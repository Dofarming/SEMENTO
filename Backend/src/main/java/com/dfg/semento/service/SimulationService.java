package com.dfg.semento.service;

import com.dfg.semento.dto.*;
import com.dfg.semento.dto.request.DateAndOhtRequest;
import com.dfg.semento.dto.request.SimulationRequest;
import com.dfg.semento.dto.response.ClassificationLogResponse;
import com.dfg.semento.dto.response.ComparedDataResponse;
import com.dfg.semento.dto.response.ComparedWorkPerTimeResponse;
import com.dfg.semento.dto.response.SimulationLogResponse;
import com.dfg.semento.exception.RestApiException;
import com.dfg.semento.exception.errorCode.CommonErrorCode;
import com.dfg.semento.exception.errorCode.ErrorCode;
import com.dfg.semento.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.*;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.composite.CompositeAggregation;
import org.elasticsearch.search.aggregations.bucket.composite.CompositeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.composite.ParsedComposite;
import org.elasticsearch.search.aggregations.bucket.composite.TermsValuesSourceBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SimulationService {

    @Value("${elasticsearch.bucket-size}")
    private int bucketSize;
    @Value("${oht.cnt}")
    private int ohtCnt;
    private final DashboardService dashboardService;
    private final ElasticsearchQueryUtil elasticsearchQueryUtil;
    private Long runningOhtCnt;
    private int minuteRange = 300; //페이징해서 가져올 시뮬레이션 데이터 범위, 분당 60

    /** 시간대별 작업량 평균 비교하는 메소드
     * @author 최서현
     */
    public ComparedWorkPerTimeResponse getCompareWork(DateAndOhtRequest dateAndOht) throws IOException {
        LocalDateTime startTime = dateAndOht.getStartDate();
        LocalDateTime endTime = dateAndOht.getEndDate();
        Long ohtId = dateAndOht.getOhtId();

        FilterAggregationBuilder specificOhtCount = AggregationBuilders.filter("specific_oht_count",
                QueryBuilders.boolQuery()
                        .must(QueryBuilders.termQuery("oht_id", ohtId))
                        .must(QueryBuilders.termQuery("status.keyword", "W"))
        ).subAggregation(
                AggregationBuilders.cardinality("unique_starts").field("start_time")
        );

        FilterAggregationBuilder overallOhtCount = AggregationBuilders.filter("overall_oht_count",
                QueryBuilders.termQuery("status.keyword", "W")
        ).subAggregation(
                AggregationBuilders.cardinality("unique_starts").field("start_time")
        );

        DateHistogramAggregationBuilder dateAgg = AggregationBuilders.dateHistogram("hours")
                .field("curr_time")
                .calendarInterval(DateHistogramInterval.HOUR) // 시간 간격 설정
                .timeZone(ZoneId.of("Asia/Seoul"))
                .subAggregation(specificOhtCount)
                .subAggregation(overallOhtCount);

        SearchResponse searchResponse = elasticsearchQueryUtil.sendEsQuery(startTime, endTime, dateAgg);
        Histogram histogram = searchResponse.getAggregations().get("hours");


        List<WorkPerTime> list = new ArrayList<>();
        for (Histogram.Bucket bucket : histogram.getBuckets()) {
            String keyAsString = bucket.getKeyAsString();

            Aggregations specificAggs = ((Filter) bucket.getAggregations().get("specific_oht_count")).getAggregations();
            long specificCount = ((Cardinality) specificAggs.get("unique_starts")).getValue();

            Aggregations overallAggs = ((Filter) bucket.getAggregations().get("overall_oht_count")).getAggregations();
            long overallCount = ((Cardinality) overallAggs.get("unique_starts")).getValue();

            list.add(WorkPerTime.builder()
                    .time(TimeConverter.convertUtcToAsia(keyAsString))
                    .me((int) specificCount)
                    .average((int) ((int) overallCount/runningOhtCnt)).build());
        }

        return ComparedWorkPerTimeResponse.builder().workPerTime(list).build();
    }

    /** 개별OHT와 모든OHT의 비교데이터를 구하는 메소드
     * @author 최서현
     */
    public ComparedDataResponse getComparedDate(DateAndOhtRequest dateAndOht) throws IOException {
        LocalDateTime startTime = dateAndOht.getStartDate();
        LocalDateTime endTime = dateAndOht.getEndDate();
        Long ohtId = dateAndOht.getOhtId();

        //평균위한 Running oht 갯수 초기화
        runningOhtCnt = setRunningOhtCnt(startTime, endTime);
        System.out.println("runningOht 갯수"+runningOhtCnt);
        if(runningOhtCnt == 0) throw new RestApiException(CommonErrorCode.NO_RUNNING_OHT);

        //각 검색결과 얻기
        IntegerDataDto totalWorkData = totalWorkComparison(startTime, endTime, ohtId);
        IntegerDataDto deadLineData = outOfDeadLineComparison(startTime, endTime, ohtId);
        DoubleDataDto speedData = averageSpeedComparison(startTime, endTime, ohtId);
        IntegerDataDto errorData = ohtErrorComparison(startTime, endTime, ohtId);

        return ComparedDataResponse.builder()
                .totalWork(totalWorkData)
                .outOfDeadLine(deadLineData)
                .averageSpeed(speedData)
                .ohtError(errorData).build();
    }


    /** 해당기간동안 일을 한 OHT들의 갯수를 구하는 메소드(평균구할때 사용)
     * @author 최서현
     */
    public Long setRunningOhtCnt(LocalDateTime startTime, LocalDateTime endTime) throws IOException {
        // 고유 oht_id 수를 계산하기 위한 cardinality 집계 생성
        CardinalityAggregationBuilder uniqueOhtIdCount = AggregationBuilders.cardinality("unique_oht_count")
                .field("oht_id");

        // 판단필터 생성 => 운행중이었던것만 체크
        TermQueryBuilder statusFilter = QueryBuilders.termQuery("status.keyword", "G");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .filter(statusFilter);

        // 쿼리날림
        SearchResponse searchResponse = elasticsearchQueryUtil.sendEsQuery(startTime, endTime, boolQueryBuilder, uniqueOhtIdCount);

        // 집계 결과 추출
        Cardinality result = searchResponse.getAggregations().get("unique_oht_count");
        Long runningOhtCnt = result.getValue();

        return runningOhtCnt;
    }


    /** 개별 OHT의 작업량과 전체 OHT 평균작업량 비교
     * @author 최서현
     */
    public IntegerDataDto totalWorkComparison(LocalDateTime startTime, LocalDateTime endTime, Long ohtId) throws IOException {
        // 전체 OHT 작업량 평균
        double overallTotalWorkAvg = (double) dashboardService.getOhtTotalWorkByStartTimeAndEndTime(startTime, endTime) / runningOhtCnt;

        // 작업 중인 로그만 검색하도록 설정
        TermQueryBuilder statusFilter = QueryBuilders.termQuery("status.keyword", "W");
        // 특정 OHT_ID만 필터링
        TermQueryBuilder ohtIdFilter = QueryBuilders.termQuery("oht_id", ohtId);

        // Bool Query로  두 filter 적용
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .filter(ohtIdFilter)
                .filter(statusFilter);

        // ==== 집계 검색 ====
        TermsAggregationBuilder termsAggregation = AggregationBuilders.terms("by_oht_id").field("oht_id");
        CardinalityAggregationBuilder cardinalityAggregation = AggregationBuilders.cardinality("count_by_oht_id").field("start_time");
        termsAggregation.subAggregation(cardinalityAggregation);

        // 쿼리날림
        SearchResponse searchResponse = elasticsearchQueryUtil.sendEsQuery(startTime, endTime, boolQueryBuilder, termsAggregation);

        // 결과에서 작업량 추출
        Terms termsAgg  = searchResponse.getAggregations().get("by_oht_id");
        int ohtTotalWork = 0;
        for(Terms.Bucket bucket: termsAgg.getBuckets()) {
            Cardinality cardinality = bucket.getAggregations().get("count_by_oht_id");
            ohtTotalWork += cardinality.getValue();
        }

        double differencePercentage = CalculateData.getDifferencePercentage(ohtTotalWork, overallTotalWorkAvg);
        return IntegerDataDto.builder().data(ohtTotalWork).percent(differencePercentage).build();
    }

    /** 개별 OHT의 에러 갯수과 전체 OHT 에러갯수 비교
     * @author 최서현
     */
    public IntegerDataDto ohtErrorComparison(LocalDateTime startTime, LocalDateTime endTime, Long ohtId) throws IOException {

        //Composite(집계) 설정
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.scriptQuery(new Script("doc['error'].value != 0")));

        //Composite Source 설정
        CompositeAggregationBuilder compositeAgg = AggregationBuilders.composite(
                "unique_combinations",
                Arrays.asList(
                        new TermsValuesSourceBuilder("oht_id").field("oht_id").missingBucket(true),
                        new TermsValuesSourceBuilder("error").field("error").missingBucket(true),
                        new TermsValuesSourceBuilder("start_time").field("start_time").missingBucket(true)

                ))
                .size(1000);


        // 결과 저장을 위한 리스트 초기화
        List<CompositeAggregation.Bucket> allBuckets = new ArrayList<>();
        Map<String, Object> afterKey = null;

        do {
            // afterKey 설정
            if (afterKey != null) {
                compositeAgg.aggregateAfter(afterKey);
            }

            // ElasticSearch 요청 및 응답
            SearchResponse searchResponse = elasticsearchQueryUtil.sendEsQuery(startTime, endTime, boolQuery, compositeAgg);

            // 결과에서 집계 데이터 추출
            CompositeAggregation compositeAggregation = searchResponse.getAggregations().get("unique_combinations");

            // 현재 집계 결과 추가
            allBuckets.addAll(compositeAggregation.getBuckets());

            // 다음 after_key 업데이트
            afterKey = compositeAggregation.afterKey();

        } while (afterKey != null);

        int ohtError = 0;
        for (CompositeAggregation.Bucket bucket : allBuckets) {
            Map<String, Object> bucketKey = bucket.getKey();
            if ((int) bucketKey.get("oht_id") == ohtId.intValue()) {
                ohtError++;
            }
        }

        System.out.println("엄마 "+allBuckets.size()+", "+runningOhtCnt+", "+ohtError);
        int overallError = allBuckets.size();
        double overallErrorAvg = (double) overallError / runningOhtCnt;
        double differencePercentage = CalculateData.getDifferencePercentage(ohtError, overallErrorAvg);

        return IntegerDataDto.builder().data(ohtError).percent(differencePercentage).build();
    }

    /** DeadLine을 넘은 작업량
     * @author 최서현
     */
    public IntegerDataDto outOfDeadLineComparison(LocalDateTime startTime, LocalDateTime endTime, Long ohtId) throws IOException {
        RangeQueryBuilder timeFilter = ElasticsearchQueryUtil.generatedTimeFilter(startTime, endTime);

        // 전체 DeadLine 카운트 및 oht_id의 고유 개수를 위한 설정
        CompositeAggregationBuilder overallCount = AggregationBuilders.composite("overall_count",
                List.of(
                        new TermsValuesSourceBuilder("oht_id").field("oht_id"),
                        new TermsValuesSourceBuilder("start_time").field("start_time")
                ))
                .size(bucketSize);

        // 특정 OHT DeadLine 카운트를 위한 설정
        FilterAggregationBuilder ohtCountFilter = AggregationBuilders.filter("oht_"+String.valueOf(ohtId)+"_count",
                        QueryBuilders.boolQuery()
                                .must(QueryBuilders.termQuery("oht_id", ohtId))
                                .must(timeFilter))
                                .subAggregation(AggregationBuilders.composite("oht_count",
                                    List.of(
                                            new TermsValuesSourceBuilder("oht_id").field("oht_id"),
                                            new TermsValuesSourceBuilder("start_time").field("start_time")
                                    ))
                                .size(bucketSize)
                                );

        BoolQueryBuilder filter = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("status.keyword", "W"))
                .must(QueryBuilders.termQuery("is_fail", true));

        SearchResponse searchResponse = elasticsearchQueryUtil.sendEsQuery(startTime, endTime, filter,
                overallCount, ohtCountFilter);

        // 집계 결과 추출
        Aggregations aggregations = searchResponse.getAggregations();
        ParsedComposite overallResults = aggregations.get("overall_count");
        ParsedComposite ohtResults = ((ParsedFilter)aggregations.get("oht_" + ohtId + "_count")).getAggregations().get("oht_count");

        int overallCnt = overallResults.getBuckets().size();
        int ohtCnt = ohtResults.getBuckets().size();
        int overallCntAvg = overallCnt/runningOhtCnt.intValue();

        //차이 퍼센티지 구함
        double differencePercentage = CalculateData.getDifferencePercentage(ohtCnt, overallCntAvg);
        return IntegerDataDto.builder().data(ohtCnt).percent(differencePercentage).build();
    }

    /** 개별 OHT의 평균속도와 전체 OHT 평균속도 비교
     * @author 최서현
     */
    public DoubleDataDto averageSpeedComparison(LocalDateTime startTime, LocalDateTime endTime, Long ohtId) throws IOException {

        // 전체 평균속도 계산 집계
        AvgAggregationBuilder overallAvgSpeed = AggregationBuilders.avg("overall_average_speed").field("speed");
        // 특정호기 OHT 평균속도 계산을 위한 집계
        AvgAggregationBuilder ohtAvgSpeed = AggregationBuilders.avg("oht_"+String.valueOf(ohtId)+"_average_speed").field("speed");

        // 특정호기 OHT에 대한 필터 생성
        FilterAggregationBuilder ohtFilter = AggregationBuilders
                .filter("filter_oht_"+String.valueOf(ohtId), QueryBuilders.termQuery("oht_id", ohtId))
                .subAggregation(ohtAvgSpeed);

        //요청 및 응답받음
        SearchResponse searchResponse = elasticsearchQueryUtil.sendEsQuery(startTime, endTime,
                overallAvgSpeed, ohtFilter);

        // 결과에서 전체 및 특정호기 평균속도를 추출
        Avg overallAvg = searchResponse.getAggregations().get("overall_average_speed");
        Filter filter = searchResponse.getAggregations().get("filter_oht_"+String.valueOf(ohtId));
        Avg ohtAvg = filter.getAggregations().get("oht_"+String.valueOf(ohtId)+"_average_speed");

        //차이 퍼센티지 구함
        double differencePercentage = CalculateData.getDifferencePercentage(ohtAvg.getValue(), overallAvg.getValue());
        return DoubleDataDto.builder().data(ohtAvg.value()).percent(differencePercentage).build();

    }

    /** 개별 OHT 작업별로 분류
     * @author 최서현
     */
    public ClassificationLogResponse getClassificationLog(DateAndOhtRequest dateAndOht) throws IOException {
        LocalDateTime startTime = dateAndOht.getStartDate();
        LocalDateTime endTime = dateAndOht.getEndDate();
        Long ohtId = dateAndOht.getOhtId();

        //==bool Query==
        ExistsQueryBuilder existsQuery = QueryBuilders.existsQuery("start_time");
        TermQueryBuilder termQuery = QueryBuilders.termQuery("oht_id", ohtId);

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .must(existsQuery)
                .must(termQuery);

        //==TermsAggregationBuilder==
        TermsAggregationBuilder byStartTime = AggregationBuilders.terms("by_start_time")
                .field("start_time")
                .size(10000)
                .order(BucketOrder.key(true));
        MaxAggregationBuilder maxCurrTime = AggregationBuilders.max("max_curr_time").field("curr_time");
        TermsAggregationBuilder errors = AggregationBuilders.terms("errors")
                                                            .field("error")
                                                            .size(10000)
                                                            .order(BucketOrder.key(true));
        AvgAggregationBuilder averageSpeed = AggregationBuilders.avg("average_speed").field("speed");
        MaxAggregationBuilder maxIsFail = AggregationBuilders.max("max_is_fail").field("is_fail");

        byStartTime.subAggregation(maxCurrTime)
                .subAggregation(errors)
                .subAggregation(averageSpeed)
                .subAggregation(maxIsFail);

        //요청 및 응답받음
        SearchResponse searchResponse = elasticsearchQueryUtil.sendEsQuery(startTime, endTime,
                boolQuery, byStartTime);

        Terms terms = searchResponse.getAggregations().get("by_start_time");

        List<LogPerWork> logPerWorkList = new ArrayList<>();
        for (Terms.Bucket bucket : terms.getBuckets()) {
            String resultStartTime = bucket.getKeyAsString();
            Max resultEndTime = bucket.getAggregations().get("max_curr_time");
            Avg resultAverageSpeed = bucket.getAggregations().get("average_speed");
            Max resultMaxIsFail = bucket.getAggregations().get("max_is_fail");
            Terms resultErrors = bucket.getAggregations().get("errors");

            List<Integer> errorList = new ArrayList<>();
            for (Terms.Bucket errorBucket : resultErrors.getBuckets()) {
                int errorCode = Integer.parseInt(errorBucket.getKeyAsString());
                if(errorCode !=0 ) errorList.add(errorCode);
            }

            logPerWorkList.add(LogPerWork.builder()
                                        .startTime(TimeConverter.convertUtcToAsia(resultStartTime))
                                        .endTime(TimeConverter.convertUtcToAsia(resultEndTime.getValueAsString()))
                                        .ohtId(ohtId)
                                        .errors(errorList)
                                        .averageSpeed(resultAverageSpeed.getValue())
                                        .outOfDeadline(Boolean.parseBoolean(resultMaxIsFail.getValueAsString()))
                                        .build());
        }

        return ClassificationLogResponse.builder().totalCnt(terms.getBuckets().size()).logPerWork(logPerWorkList).build();
    }

    public SimulationLogResponse getSimulationLog(SimulationRequest simulationRequest) throws IOException {
        LocalDateTime startTime = simulationRequest.getStartDate();
        LocalDateTime endTime = simulationRequest.getEndDate();
        List<Long> ohtList = simulationRequest.getOhtId();

        // Query
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (ohtList != null && !ohtList.isEmpty()) {
            TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("oht_id", ohtList);
            boolQueryBuilder.must(termsQueryBuilder);
        }

        // Aggregations
        TermsAggregationBuilder logsByCurrTime = AggregationBuilders.terms("logs_by_curr_time")
                .field("curr_time").size(minuteRange);//1분에 60개, 5분에 300개

        TopHitsAggregationBuilder ohtDetails = AggregationBuilders.topHits("oht_details")
                .fetchSource(new String[]{"oht_id", "path", "curr_node", "point_x", "point_y", "status", "error", "carrier", "speed", "is_fail"}, null)
                .size(ohtCnt) //OHT 최대갯수
                .sort("curr_time", SortOrder.DESC) // Sort by curr_time in descending order
                .sort("oht_id", SortOrder.ASC); // Additional sort by oht_id in ascending order


        logsByCurrTime.subAggregation(ohtDetails);

        SearchResponse searchResponse = elasticsearchQueryUtil.sendEsQuery(startTime, endTime,
                boolQueryBuilder, logsByCurrTime);

        List<SimulationPerDate> simulationPerDates = new ArrayList<>();
        Terms logsByCurrTimeAgg = searchResponse.getAggregations().get("logs_by_curr_time");

        System.out.println(searchResponse.toString());

        for (Terms.Bucket bucket : logsByCurrTimeAgg.getBuckets()) {
            List<SimulationPerOhtDto> simulationPerOhtDtoList = new ArrayList<>();
            TopHits topHits = bucket.getAggregations().get("oht_details");

            for (SearchHit hit : topHits.getHits().getHits()) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                LocationDto locationDto = LocationDto.builder()
                        .path((String) sourceAsMap.get("path"))
                        .currNode((String) sourceAsMap.get("curr_node"))
                        .pointX((Double) sourceAsMap.get("point_x"))
                        .pointY((Double) sourceAsMap.get("point_y"))
                        .build();

                SimulationPerOhtDto simulationPerOhtDto = SimulationPerOhtDto.builder()
                        .ohtId(Long.parseLong(sourceAsMap.get("oht_id").toString()))
                        .location(locationDto)
                        .status((String) sourceAsMap.get("status"))
                        .carrier((Boolean) sourceAsMap.get("carrier"))
                        .error((Integer) sourceAsMap.get("error"))
                        .speed((Double) sourceAsMap.get("speed"))
                        .isFail((Boolean) sourceAsMap.get("is_fail"))
                        .build();

                simulationPerOhtDtoList.add(simulationPerOhtDto);
            }
            LocalDateTime currTime = TimeConverter.convertUtcToAsia(bucket.getKeyAsString());
            simulationPerDates.add(SimulationPerDate.builder()
                    .time(currTime)
                    .data(simulationPerOhtDtoList)
                    .build());
        }
        return SimulationLogResponse.builder().simulationLog(simulationPerDates).build();
    }
}

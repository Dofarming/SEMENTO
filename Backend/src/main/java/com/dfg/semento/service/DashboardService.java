package com.dfg.semento.service;

import com.dfg.semento.document.LogDocument;
import com.dfg.semento.dto.DoubleDataDto;
import com.dfg.semento.dto.IntegerDataDto;
import com.dfg.semento.dto.request.SearchTimeRequest;
import com.dfg.semento.dto.response.JobResultAnalysisErrorLogResponse;
import com.dfg.semento.dto.response.JobResultAnalysisErrorResponse;
import com.dfg.semento.dto.response.JobResultAnalysisRatioResponse;
import com.dfg.semento.dto.response.JobResultAnalysisResponse;
import com.dfg.semento.dto.response.OhtJobAnalysisResponse;
import com.dfg.semento.dto.response.OhtJobHourlyResponse;
import com.dfg.semento.dto.response.StateAnalysisResponse;
import com.dfg.semento.dto.response.StateHourlyAnalysisResponse;
import com.dfg.semento.dto.response.StateHourlyResponse;
import com.dfg.semento.repository.DashboardRepository;
import com.dfg.semento.util.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.ScriptQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.composite.CompositeAggregation;
import org.elasticsearch.search.aggregations.bucket.composite.CompositeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.composite.TermsValuesSourceBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Cardinality;
import org.elasticsearch.search.aggregations.metrics.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Max;
import org.elasticsearch.search.aggregations.metrics.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ScriptedMetric;
import org.elasticsearch.search.aggregations.metrics.ScriptedMetricAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.TopHits;
import org.elasticsearch.search.aggregations.metrics.TopHitsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.script.Script;

import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    @Value("${elasticsearch.bucket-size}")
    private int BUKET_SIZE;

    @Value("200")
    private int OHT_DEADLINE;

    private final ElasticsearchQueryUtil elasticsearchQueryUtil;


    /** 기간동안 OHT 작업량 분석(oht대수, 전체 작업량, oht별 작업량 평균)
     */
    public OhtJobAnalysisResponse ohtJobAnalysis(LocalDateTime startTime, LocalDateTime endTime) throws IOException {
        SearchTimeRequest lastPeriod = calculateLastPeriod(startTime, endTime);

        // 운행한 OHT 대수
        int ohtCount = getOhtCount(startTime, endTime);
        int lastOhtCount = getOhtCount(lastPeriod.getStartTime(), lastPeriod.getEndTime());

        // 기간동안 전체 OHT 작업량
        int totalWork = getOhtTotalWorkByStartTimeAndEndTime(startTime, endTime);
        int lastTotalWork = getOhtTotalWorkByStartTimeAndEndTime(lastPeriod.getStartTime(), lastPeriod.getEndTime());
        double totalWorkPercentage = CalculateData.getDifferencePercentage(totalWork, lastTotalWork);

        // 기간동안 각 OHT별 작업량 평균
        double averageWork = (double)totalWork / (double)ohtCount; //getOhtAverageWorkByStartTimeAndTime(startTime, endTime);
        double lastAverageWork = (double)lastTotalWork / (double)lastOhtCount; //getOhtAverageWorkByStartTimeAndTime(lastPeriod.getStartTime(), lastPeriod.getEndTime());
        double averageWorkPercentage = CalculateData.getDifferencePercentage(averageWork, lastAverageWork);

        return OhtJobAnalysisResponse.builder()
            .ohtCount(new IntegerDataDto(ohtCount, 0))
            .totalWork(new IntegerDataDto(totalWork, totalWorkPercentage))
            .averageWork(new DoubleDataDto(averageWork, averageWorkPercentage))
            .build();
    }

    /**
     * 기간동안 시간별 작업량 분석
     */
    public List<OhtJobHourlyResponse> ohtJobHourly(LocalDateTime startTime, LocalDateTime endTime) throws IOException {
        Map<Integer, Integer> hourlyWork = getOhtJobHourly(startTime, endTime);
        List<OhtJobHourlyResponse> response = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : hourlyWork.entrySet()) {
            response.add(OhtJobHourlyResponse.builder()
                .hour(entry.getKey())
                .work(entry.getValue())
                .build());
        }
        return response;
    }

    /**
     * 기간동안 작업 성공/실패 결과 분석
     */
    public JobResultAnalysisResponse jobResultAnalysis(LocalDateTime startTime, LocalDateTime endTime) throws
        IOException {
        // ==== 작업 성공 실패율 분석
        // [0: 전체 작업량 수, 1: 성공 작업량 수, 2: 실패 작업량 수]
        JobResultAnalysisRatioResponse ratioResponse = getJobResultCount(startTime, endTime);

        // ==== 작업 실패 중 에러 발생량
        JobResultAnalysisResponse errorResult = getJobErrorCount(startTime, endTime);

        return JobResultAnalysisResponse.builder()
            .jobResultRatio(ratioResponse)
            .jobResultError(errorResult.getJobResultError())
            .jobResultErrorLog(errorResult.getJobResultErrorLog())
            .build();
    }

    /**
     * 전체 OHT 상태 분석 (데드라인, 평균 작업 시간, 평균 유휴 시간)
     */
    public StateAnalysisResponse stateAnalysis(LocalDateTime startTime, LocalDateTime endTime) throws IOException {
        SearchTimeRequest lastPeriod = calculateLastPeriod(startTime, endTime);
        // this month
        int totalWork = getOhtTotalWorkByStartTimeAndEndTime(startTime, endTime);
        int workingTime = getWorkingTime(startTime, endTime);
        int idleTime = getIdleTime(startTime, endTime);

        // last month
        int lastTotalWork = getOhtTotalWorkByStartTimeAndEndTime(lastPeriod.getStartTime(), lastPeriod.getEndTime());
        int lastWorkingTime = getWorkingTime(lastPeriod.getStartTime(), lastPeriod.getEndTime());
        int lastIdleTime = getIdleTime(lastPeriod.getStartTime(), lastPeriod.getEndTime());

        // 평균 작업/유휴 시간 계산
        int averageWorkingTime = totalWork == 0 ? 0 : workingTime/totalWork;
        int averageIdleTime = totalWork == 0 ? 0 : idleTime/totalWork;
        int lastAverageWorkingTime = lastTotalWork == 0 ? 0 : lastWorkingTime / lastTotalWork;
        int lastAverageIdleTime = lastTotalWork == 0 ? 0 : lastIdleTime / lastTotalWork;

        return StateAnalysisResponse.builder()
            .deadline(new IntegerDataDto(OHT_DEADLINE, 0))
            .averageWorkTime(new IntegerDataDto(
                averageWorkingTime,
                CalculateData.getDifferencePercentage(averageWorkingTime, lastAverageWorkingTime)))
            .averageIdleTime(new IntegerDataDto(
                averageIdleTime,
                CalculateData.getDifferencePercentage(averageIdleTime, lastAverageIdleTime)))
            .build();
    }

    /**
     * 기간동안 평균적으로 시간대별 작업/유휴 상태 OHT 수 계산 및 집계(작업이 많은, OHT가 활발한, 유휴상태가 많은)
     */
    public StateHourlyAnalysisResponse stateHourlyAnalysis(LocalDateTime startTime, LocalDateTime endTime) throws
        IOException {
        // 기간동안 시간대별 총 작업/유휴 상태 OHT 수
        Map<Integer, Integer> workHourCount = getWorkStateHourly(startTime, endTime);
        Map<Integer, Integer> idleHourCount = getIdleStateHourly(startTime, endTime);

        // 평균 계산을 위한 기간동안 일수
        long day = ChronoUnit.DAYS.between(startTime, endTime) + 1;

        // workHourCount -> response 변환
        List<StateHourlyResponse> workHourCountResponse = new ArrayList<>();
        int maxWorkHour = 0;
        for (Map.Entry<Integer, Integer> entry : workHourCount.entrySet()) {
            if(workHourCount.get(maxWorkHour) < entry.getValue()) maxWorkHour = entry.getKey();
            workHourCountResponse.add(StateHourlyResponse.builder()
                .hour(entry.getKey())
                .count((double)entry.getValue() / (double)day)
                .build());
        }

        // idleHourCount -> response 변환
        List<StateHourlyResponse> idleHourCountResponse = new ArrayList<>();
        int maxIdleHour = 0;
        for (Map.Entry<Integer, Integer> entry : idleHourCount.entrySet()) {
            if(idleHourCount.get(maxIdleHour) < entry.getValue()) maxIdleHour = entry.getKey();
            idleHourCountResponse.add(StateHourlyResponse.builder()
                .hour(entry.getKey())
                .count((double)entry.getValue() / (double)day)
                .build());
        }

        // 시간대 분석
        Map<Integer, Integer> jobHourly = getOhtJobHourly(startTime, endTime);
        List<Map.Entry<Integer, Integer>> jobHourlySorted = new LinkedList<>(jobHourly.entrySet());
        jobHourlySorted.sort(Map.Entry.<Integer, Integer>comparingByValue().reversed());

        return StateHourlyAnalysisResponse.builder()
            .workHourCount(workHourCountResponse)
            .idleHourCount(idleHourCountResponse)
            .maxJobTime(jobHourlySorted.getFirst().getKey())
            .maxWorkTime(maxWorkHour)
            .maxIdleTime(maxIdleHour)
            .build();
    }

    /**
     * [Elasticsearch 검색] 기간동안 운행했던 OHT 대수 계산 함수
    */
    private int getOhtCount(LocalDateTime startTime, LocalDateTime endTime) throws IOException {
        // ==== 집계 검색 ====
        CardinalityAggregationBuilder cardinalityAggregation = AggregationBuilders.cardinality("oht_count")
            .field("oht_id");

        // ==== 질의 ====
        SearchResponse searchResponse = elasticsearchQueryUtil.sendEsQuery(startTime, endTime, cardinalityAggregation);

        // 결과에서 작업량 추출
        Cardinality ohtCount  = searchResponse.getAggregations().get("oht_count");

        return (int) ohtCount.getValue();
    }

    /**
     * [Elasticsearch 검색] 기간동안 전체 작업량 계산 함수
     */
    public int getOhtTotalWorkByStartTimeAndEndTime(LocalDateTime startTime, LocalDateTime endTime) throws
        IOException {
        // ==== 쿼리 검색 ====
        TermQueryBuilder statusFilter = QueryBuilders.termQuery("status.keyword", "W");

        // Bool Query로  두 filter 적용
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
            .filter(statusFilter);

        // ==== 집계 검색 ====
        MaxAggregationBuilder maxAggregation = AggregationBuilders.max("max_curr_time").field("curr_time");

        CompositeAggregationBuilder compositeAgg = AggregationBuilders
            .composite(
                "composite_agg",
                Arrays.asList(
                    new TermsValuesSourceBuilder("oht_id")
                        .field("oht_id"),
                    new TermsValuesSourceBuilder("start_time")
                        .field("start_time")
                ))
            .subAggregation(maxAggregation)
            .size(BUKET_SIZE);

        // ==== 데이터 저장할 자료구조 ====
        int totalWork = 0;

        // ==== 질의 ====
        Map<String, Object> afterKey = null;
        do {
            if(afterKey != null) {
                compositeAgg.aggregateAfter(afterKey);
            }
            SearchResponse searchResponse = elasticsearchQueryUtil.sendEsQuery(startTime, endTime, boolQueryBuilder, compositeAgg);
            CompositeAggregation composite = searchResponse.getAggregations().get("composite_agg");
            afterKey = composite.afterKey();

            totalWork += composite.getBuckets().size();
        } while (afterKey != null);
        return totalWork;
    }

    /**
     * [Elasticsearch 검색] 기간동안 OHT별 평균 작업량 계산 함수 (사용 X)
     */
    private double getOhtAverageWorkByStartTimeAndTime(LocalDateTime startTime, LocalDateTime endTime) throws
        IOException {
        // ==== 쿼리 검색 ====
        // 작업 중인 로그만 검색하도록 설정
        TermQueryBuilder statusFilter = QueryBuilders.termQuery("status.keyword", "W");

        // Bool Query로  두 filter 적용
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
            .filter(statusFilter);

        // ==== 집계 검색 ====
        TermsAggregationBuilder termsAggregation = AggregationBuilders.terms("by_oht_id")
            .field("oht_id");

        CardinalityAggregationBuilder cardinalityAggregation = AggregationBuilders.cardinality("count_by_oht_id")
            .field("start_time");

        // oht_id 별 start_time 개수세는 쿼리 추가
        termsAggregation.subAggregation(cardinalityAggregation);

        // ==== 질의 ====
        SearchResponse searchResponse = elasticsearchQueryUtil.sendEsQuery(startTime, endTime, boolQueryBuilder, termsAggregation);

        // 결과에서 작업량 추출
        Terms termsAgg  = searchResponse.getAggregations().get("by_oht_id");

        // 평균 계산
        if(termsAgg.getBuckets().isEmpty()) return 0;
        double sum = 0;
        for(Terms.Bucket bucket: termsAgg.getBuckets()) {
            Cardinality cardinality = bucket.getAggregations().get("count_by_oht_id");
            sum += cardinality.getValue();
        }
        return sum / termsAgg.getBuckets().size();
    }

    /**
     * [Elasticsearch 검색] 기간동안 시간대별 작업량
     */
    public Map<Integer, Integer> getOhtJobHourly(LocalDateTime startTime, LocalDateTime endTime) throws IOException {
        // ==== 쿼리 검색 ====
        TermQueryBuilder statusFilter = QueryBuilders.termQuery("status.keyword", "W");
        MatchQueryBuilder carrierFilter = QueryBuilders.matchQuery("carrier", false);
        ScriptQueryBuilder scriptFilter = QueryBuilders.scriptQuery(
            new Script("doc['current_node.keyword'].value == doc['target_node.keyword'].value")
        );

        // Bool Query로  두 filter 적용
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
            .filter(statusFilter)
            .filter(carrierFilter)
            .filter(scriptFilter);

        // ==== 집계 검색 ====
        MaxAggregationBuilder maxAggregation = AggregationBuilders.max("max_curr_time").field("curr_time");

        CompositeAggregationBuilder compositeAgg = AggregationBuilders
            .composite(
                "composite_agg",
                Arrays.asList(
                    new TermsValuesSourceBuilder("oht_id")
                        .field("oht_id"),
                    new TermsValuesSourceBuilder("start_time")
                        .field("start_time")
                ))
            .subAggregation(maxAggregation)
            .size(BUKET_SIZE);

        // ==== 데이터 저장할 자료구조 ====
        Map<Integer, Integer> hourCounts = new HashMap<>();
        for(int i=0; i<=23; i++) {
            hourCounts.put(i, 0);
        }

        // ==== 질의 ====
        Map<String, Object> afterKey = null;
        do {
            if(afterKey != null) {
                compositeAgg.aggregateAfter(afterKey);
            }
            SearchResponse searchResponse = elasticsearchQueryUtil.sendEsQuery(startTime, endTime, boolQueryBuilder, compositeAgg);
            CompositeAggregation composite = searchResponse.getAggregations().get("composite_agg");
            afterKey = composite.afterKey();

            for (CompositeAggregation.Bucket bucket : composite.getBuckets()) {
                Max maxCurrTime = bucket.getAggregations().get("max_curr_time");
                long maxTime = (long) maxCurrTime.getValue();
                // timezone 변경
                ZonedDateTime dateTime = Instant.ofEpochMilli(maxTime).atZone(ZoneId.of("Asia/Seoul"));
                hourCounts.put(dateTime.getHour(), hourCounts.get(dateTime.getHour()) + 1);
            }
        } while (afterKey != null);

        return hourCounts;
    }

    /**
     * [Elasticsearch 검색] 전체 작업량 중 성공, 실패량 및 퍼센테이지
     */
    private JobResultAnalysisRatioResponse getJobResultCount(LocalDateTime startTime, LocalDateTime endTime) throws
        IOException {
        // ==== 쿼리 검색 ====
        TermQueryBuilder statusFilter = QueryBuilders.termQuery("status.keyword", "W");
        ScriptQueryBuilder scriptFilter = QueryBuilders.scriptQuery(
            new Script("doc['current_node.keyword'].value == doc['target_node.keyword'].value")
        );

        // Bool Query로  두 filter 적용
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
            .filter(scriptFilter)
            .filter(statusFilter);

        // ==== 집계 검색 ====
        TopHitsAggregationBuilder topHitsAggregation = AggregationBuilders.topHits("latest_record")
            .size(1)
            .sort(SortBuilders.fieldSort("curr_time").order(SortOrder.DESC))
            .fetchSource("is_fail", null);
        TermsAggregationBuilder termsAggregation = AggregationBuilders.terms("group_by_oht_id_start_time")
            .script(
                new Script("doc['oht_id'].value + ' ' + doc['start_time'].value")
            ).size(Integer.MAX_VALUE)
            .subAggregation(topHitsAggregation);

        // ==== 질의 ====
        SearchResponse searchResponse = elasticsearchQueryUtil.sendEsQuery(startTime, endTime, boolQueryBuilder, termsAggregation);

        // ==== 결과에서 개수 추출 ====
        Terms termsAgg  = searchResponse.getAggregations().get("group_by_oht_id_start_time");

        int totalWork = termsAgg.getBuckets().size();
        int successWork = 0;
        int failedWork = 0;

        for(Terms.Bucket bucket: termsAgg.getBuckets()) {
            TopHits topHits = bucket.getAggregations().get("latest_record");
            boolean isFail = (boolean) topHits.getHits().getHits()[0].getSourceAsMap().get("is_fail");
            if(isFail) failedWork++;
            else successWork++;
        }

        return JobResultAnalysisRatioResponse.builder()
            .totalWork(totalWork)
            .successWork(successWork)
            .failedWork(failedWork)
            .successPercentage(CalculateData.getPercentage(successWork, totalWork))
            .failedPercentage(CalculateData.getPercentage(failedWork, totalWork))
            .build();
    }

    /**
     * [Elasticsearch 검색] 실패한 작업 중 에러 발생 작업 카운트
     */
    private JobResultAnalysisResponse getJobErrorCount(LocalDateTime startTime, LocalDateTime endTime) throws IOException {
        //Composite(집계) 설정
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.mustNot(QueryBuilders.matchQuery("error", 0));

        //Composite Source 설정
        CompositeAggregationBuilder compositeAgg = AggregationBuilders.composite(
                "unique_combinations",
                Arrays.asList(
                    new TermsValuesSourceBuilder("oht_id").field("oht_id").missingBucket(true),
                    new TermsValuesSourceBuilder("current_node").field("current_node.keyword").missingBucket(true),
                    new TermsValuesSourceBuilder("error").field("error").missingBucket(true),
                    new TermsValuesSourceBuilder("start_time").field("start_time").missingBucket(true)

                ))
            .size(BUKET_SIZE);

        // ==== 데이터 저장할 자료구조 ====
        // 에러 수 집계용
        int ohtError = 0;
        int facilityError = 0;
        int totalError = 0;

        // oht_id 별 에러 수 집계
        Map<String, Integer> countErrorPerOht = new HashMap<>();

        // ==== 질의 ====
        Map<String, Object> afterKey = null;
        do {
            if(afterKey != null) {
                compositeAgg.aggregateAfter(afterKey);
            }
            SearchResponse searchResponse = elasticsearchQueryUtil.sendEsQuery(startTime, endTime, boolQuery, compositeAgg);
            CompositeAggregation compositeAggregation = searchResponse.getAggregations().get("unique_combinations");
            afterKey = compositeAggregation.afterKey();

            totalError += compositeAggregation.getBuckets().size();
            // 각 버킷에서 에러 수 추출
            for (CompositeAggregation.Bucket bucket : compositeAggregation.getBuckets()) {
                Map<String, Object> bucketKey = bucket.getKey();
                int ohtId = (int) bucketKey.get("oht_id");
                Object errorObj = bucketKey.get("error");
                int error = 0;
                if(errorObj instanceof Integer) error = (int) errorObj;
                else if(errorObj instanceof String) error = Integer.parseInt((String) errorObj);
                switch (error) {
                    case 200:
                        ohtError++;
                        break;
                    case 300:
                        facilityError++;
                        break;
                }
                String key = ohtId+" "+error;
                countErrorPerOht.put(key, countErrorPerOht.getOrDefault(key,  0)+1);
            }
        } while (afterKey != null);

        // 에러 집계 Response로 변환
        JobResultAnalysisErrorResponse errorResponse = JobResultAnalysisErrorResponse
            .builder()
            .totalError(totalError)
            .facilityError(facilityError)
            .ohtError(ohtError)
            .facilityErrorPercentage(CalculateData.getPercentage(facilityError, totalError))
            .ohtErrorPercentage(CalculateData.getPercentage(ohtError, totalError))
            .build();

        // 로그 Response로 변환
        List<JobResultAnalysisErrorLogResponse> logResponses = new ArrayList<>();
        StringTokenizer st;
        for(Map.Entry<String, Integer> entry : countErrorPerOht.entrySet()) {
            st = new StringTokenizer(entry.getKey());
            int ohtId = Integer.parseInt(st.nextToken());
            int error = Integer.parseInt(st.nextToken());
            int count = entry.getValue();
            logResponses.add(
                JobResultAnalysisErrorLogResponse.builder()
                    .ohtId(ohtId)
                    .error(error)
                    .count(count)
                    .build()
            );
        }
        return JobResultAnalysisResponse.builder()
            .jobResultError(errorResponse)
            .jobResultErrorLog(logResponses)
            .build();
    }


    /**
     * [Elasticsearch 검색] OHT가 Idle인 시간 합산
     */
    private int getIdleTime(LocalDateTime startTime, LocalDateTime endTime) throws IOException {
        // ==== 쿼리 검색 ====
        TermQueryBuilder statusFilter = QueryBuilders.termQuery("status.keyword", "I");

        // Bool Query로  두 filter 적용
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
            .must(statusFilter);

        // ==== 집계 검색 ====
        CardinalityAggregationBuilder cardinalityAggregation = AggregationBuilders
            .cardinality("count_working")
            .script(new Script("doc['oht_id'].value + ' ' + doc['curr_time'].value"));

        // ==== 질의 ====
        SearchResponse searchResponse = elasticsearchQueryUtil.sendEsQuery(startTime, endTime, boolQueryBuilder, cardinalityAggregation);

        // ==== 결과에서 개수 추출 ====
        Cardinality workingCount  = searchResponse.getAggregations().get("count_working");

        return (int) workingCount.getValue();
    }

    /**
     * [Elasticsearch 검색] OHT가 작업 중인(Arrived, Working, Going) 시간 합산
     */
    private int getWorkingTime(LocalDateTime startTime, LocalDateTime endTime) throws IOException {
        // ==== 쿼리 검색 ====
        TermQueryBuilder statusFilter = QueryBuilders.termQuery("status.keyword", "I");

        // Bool Query로  두 filter 적용
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
            .mustNot(statusFilter);

        // ==== 집계 검색 ====
        CardinalityAggregationBuilder cardinalityAggregation = AggregationBuilders
            .cardinality("count_working")
            .script(new Script("doc['oht_id'].value + ' ' + doc['curr_time'].value"));

        // ==== 질의 ====
        SearchResponse searchResponse = elasticsearchQueryUtil.sendEsQuery(startTime, endTime, boolQueryBuilder, cardinalityAggregation);

        // ==== 결과에서 개수 추출 ====
        Cardinality workingCount  = searchResponse.getAggregations().get("count_working");

        return (int) workingCount.getValue();
    }


    /**
     * [Elasticsearch 검색] 시간대별 작업 상태 OHT 개수 계산
     */
    private Map<Integer, Integer> getWorkStateHourly(LocalDateTime startTime, LocalDateTime endTime) throws IOException {
        // ==== 쿼리 검색 ====
        TermQueryBuilder statusFilter = QueryBuilders.termQuery("status.keyword", "I");

        // Bool Query로  두 filter
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
            .mustNot(statusFilter);

        // ==== 집계 검색 ====
        MaxAggregationBuilder maxAggregation = AggregationBuilders.max("max_curr_time").field("curr_time");
        CompositeAggregationBuilder compositeAgg = AggregationBuilders
            .composite(
                "composite_agg",
                Arrays.asList(
                    new TermsValuesSourceBuilder("oht_id")
                        .field("oht_id"),
                    new TermsValuesSourceBuilder("curr_time")
                        .script(new Script("doc['curr_time'].value.toInstant().atZone(ZoneId.of('Asia/Seoul')).format(DateTimeFormatter.ofPattern('yyyy-MM-dd HH'))"))
                ))
            .subAggregation(maxAggregation)
            .size(BUKET_SIZE);

        // ==== 데이터 저장할 자료구조 ====
        Map<Integer, Integer> hourCounts = new HashMap<>();
        for(int i=0; i<=23; i++) {
            hourCounts.put(i, 0);
        }

        // ==== 질의 ====
        Map<String, Object> afterKey = null;
        do {
            if(afterKey != null) {
                compositeAgg.aggregateAfter(afterKey);
            }
            SearchResponse searchResponse = elasticsearchQueryUtil.sendEsQuery(startTime, endTime, boolQueryBuilder, compositeAgg);
            CompositeAggregation composite = searchResponse.getAggregations().get("composite_agg");
            afterKey = composite.afterKey();

            for (CompositeAggregation.Bucket bucket : composite.getBuckets()) {
                Max maxCurrTime = bucket.getAggregations().get("max_curr_time");
                long maxTime = (long) maxCurrTime.getValue();
                // timezone 변경
                ZonedDateTime dateTime = Instant.ofEpochMilli(maxTime).atZone(ZoneId.of("Asia/Seoul"));
                hourCounts.put(dateTime.getHour(), hourCounts.get(dateTime.getHour()) + 1);
            }
        } while (afterKey != null);

        return hourCounts;
    }

    /**
     * [Elasticsearch 검색] 시간대별 유휴 상태 OHT 개수 계산
     */
    private Map<Integer, Integer> getIdleStateHourly(LocalDateTime startTime, LocalDateTime endTime) throws IOException {
        // ==== 쿼리 검색 ====
        TermQueryBuilder statusFilter = QueryBuilders.termQuery("status.keyword", "I");

        // Bool Query로  두 filter
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
            .must(statusFilter);

        // ==== 집계 검색 ====
        MaxAggregationBuilder maxAggregation = AggregationBuilders.max("max_curr_time").field("curr_time");
        CompositeAggregationBuilder compositeAgg = AggregationBuilders
            .composite(
                "composite_agg",
                Arrays.asList(
                    new TermsValuesSourceBuilder("oht_id")
                        .field("oht_id"),
                    new TermsValuesSourceBuilder("curr_time")
                        .script(new Script("doc['curr_time'].value.toInstant().atZone(ZoneId.of('Asia/Seoul')).format(DateTimeFormatter.ofPattern('yyyy-MM-dd HH'))"))
                ))
            .subAggregation(maxAggregation)
            .size(BUKET_SIZE);

        // ==== 데이터 저장할 자료구조 ====
        Map<Integer, Integer> hourCounts = new HashMap<>();
        for(int i=0; i<=23; i++) {
            hourCounts.put(i, 0);
        }

        // ==== 질의 ====
        Map<String, Object> afterKey = null;
        do {
            if(afterKey != null) {
                compositeAgg.aggregateAfter(afterKey);
            }
            SearchResponse searchResponse = elasticsearchQueryUtil.sendEsQuery(startTime, endTime, boolQueryBuilder, compositeAgg);
            CompositeAggregation composite = searchResponse.getAggregations().get("composite_agg");
            afterKey = composite.afterKey();

            for (CompositeAggregation.Bucket bucket : composite.getBuckets()) {
                Max maxCurrTime = bucket.getAggregations().get("max_curr_time");
                long maxTime = (long) maxCurrTime.getValue();
                // timezone 변경
                ZonedDateTime dateTime = Instant.ofEpochMilli(maxTime).atZone(ZoneId.of("Asia/Seoul"));
                hourCounts.put(dateTime.getHour(), hourCounts.get(dateTime.getHour()) + 1);
            }
        } while (afterKey != null);

        return hourCounts;
    }


    /**
     * startTime, endTime을 기준으로 지난 기간의 시작, 종료 시간 계산하기
     */
    private SearchTimeRequest calculateLastPeriod(LocalDateTime startTime, LocalDateTime endTime) {
        // 현재 시간에서 1달 전의 년도와 월 계산
        int lastMonthYear = startTime.minusMonths(1).getYear();
        int lastMonth = startTime.minusMonths(1).getMonthValue();

        // 지난 달의 첫 번째 날과 마지막 날 구하기
        LocalDateTime firstDayOfLastMonth = LocalDateTime.of(LocalDate.of(lastMonthYear, lastMonth, 1), LocalTime.MIN);
        LocalDateTime lastDayOfLastMonth = LocalDateTime.of(LocalDate.of(lastMonthYear, lastMonth, 1).withDayOfMonth(1).plusMonths(1).minusDays(1), LocalTime.MAX);
        return new SearchTimeRequest(firstDayOfLastMonth, lastDayOfLastMonth);
    }
}

package com.dfg.semento.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ElasticsearchQueryUtil {
    private final RestHighLevelClient client;

    /** ES의 검색할 인덱스명을 생성하는 메소드
     * @author 최서현
     * @param startTime 시작시간
     * @param endTime 종료시간
     * @return String[] 검색할 인덱스명 배열
     */
    public static String[] getIndexNameArray(LocalDateTime startTime, LocalDateTime endTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = startTime.toLocalDate();
        LocalDate end = endTime.toLocalDate();

        // 인덱스 리스트를 생성
        List<String> indices = new ArrayList<>();
        while (!start.isAfter(end)) {
            // log.debug("인덱스명 [semento-mysql-logs-" + start.format(formatter)+"] 생성됨.");
            indices.add("semento-mysql-logs-" + start.format(formatter));
            start = start.plusDays(1);
        }

        return indices.toArray(new String[0]);  // 검색 요청에 사용할 인덱스 배열
    }

    /** ES의 검색할 시간을 원하는 포맷으로 설정하는 메소드
     * @author 최서현
     * @param startTime 시작시간
     * @param endTime 종료시간
     * @return RangeQueryBuilder 시간검색쿼리
     */
    public static RangeQueryBuilder generatedTimeFilter(LocalDateTime startTime, LocalDateTime endTime){
        //시간 포맷 변환
        FormattedTime formattedTime = TimeConverter.convertElasticsearchTime(startTime, endTime);

        // startTime과 endTime 사이에 있는 로그만 검색하도록 설정
        RangeQueryBuilder timeFilter = QueryBuilders.rangeQuery("curr_time")
                .gte(formattedTime.getStartTime())
                .lte(formattedTime.getEndTime());

        return timeFilter;
    }


    /**
     * Elasticsearch 쿼리를 전송하는 일반화된 메소드
     * @author 최서현
     * @param startTime 시작시간
     * @param endTime 종료시간
     * @param boolQueryBuilder 조건
     * @param aggregations 집계조건들
     * @return 검색 응답
     * @throws IOException 네트워크 또는 ES 서버에서 예외 발생 시
     */
    public SearchResponse sendEsQuery(LocalDateTime startTime, LocalDateTime endTime, BoolQueryBuilder boolQueryBuilder, AggregationBuilder... aggregations) throws IOException {
        //인덱스 리스트 생성
        String[] indexArray = getIndexNameArray(startTime, endTime);

        // index가 존재하지 않으면 생성하기
        for (String indexName : indexArray) {
            ensureIndexExists(indexName);
        }

        //시간필터 생성
        RangeQueryBuilder timeFilter = generatedTimeFilter(startTime, endTime);
        boolQueryBuilder.must(timeFilter);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(boolQueryBuilder)
                .size(0);  // 문서 반환 없이 집계 결과만 반환

        for(AggregationBuilder aggregation : aggregations) searchSourceBuilder.aggregation(aggregation);

        SearchRequest searchRequest = new SearchRequest(indexArray);
        searchRequest.source(searchSourceBuilder);

        log.debug("[ES request] : "+ startTime + " =>" + endTime);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        // log.debug("[ES response] : "+searchResponse.toString());
        return searchResponse;
    }

    /**
     * Elasticsearch 쿼리를 전송하는 일반화된 메소드
     * @author 최서현
     * @param startTime 검색시작시간
     * @param endTime 검색끝시간
     * @param aggregations 집계조건들
     * @return 검색 응답
     * @throws IOException 네트워크 또는 ES 서버에서 예외 발생 시
     */
    public SearchResponse sendEsQuery(LocalDateTime startTime, LocalDateTime endTime, AggregationBuilder... aggregations) throws IOException {
        //인덱스 리스트 생성
        String[] indexArray = getIndexNameArray(startTime, endTime);

        // index가 존재하지 않으면 생성하기
        for (String indexName : indexArray) {
            ensureIndexExists(indexName);
        }

        //시간필터 생성
        RangeQueryBuilder timeFilter = generatedTimeFilter(startTime, endTime);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(timeFilter);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
            .query(boolQueryBuilder)
            .size(0);  // 문서 반환 없이 집계 결과만 반환

        for(AggregationBuilder aggregation : aggregations) searchSourceBuilder.aggregation(aggregation);

        SearchRequest searchRequest = new SearchRequest(indexArray);
        searchRequest.source(searchSourceBuilder);

        log.debug("[ES request] : "+ startTime + " =>" + endTime);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        //log.debug("[ES response] : "+searchResponse.toString());

        return searchResponse;
    }

    /**
     * 인덱스가 존재하는지 확인 후 없으면 생성
     */
    public void ensureIndexExists(String indexName) throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
        boolean exists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);

        if (!exists) {
            // 인덱스가 존재하지 않으면 생성
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
            client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        }
    }
}

package com.dfg.semento.service;

import com.dfg.semento.dto.DetectionResultInfoDto;
import com.dfg.semento.dto.ErrorInfoDto;
import com.dfg.semento.dto.request.SearchTimeRequest;
import com.dfg.semento.dto.response.AnalyticsResponse;
import com.dfg.semento.dto.response.AnalyticsToClientResponse;
import com.dfg.semento.util.ElasticsearchQueryUtil;
import com.dfg.semento.util.TimeConverter;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsService {
    @Value("${oht.cnt}")
    private int ohtCnt;
    private final ElasticsearchQueryUtil elasticsearchQueryUtil;
    private final AiAnalyticsService aiAnalyticsService;
    public AnalyticsToClientResponse getAnalytics(SearchTimeRequest searchTimeRequest) throws IOException {
        AnalyticsResponse aiResponse = aiAnalyticsService.getAnalyticsResultByTimeAi(
                TimeConverter.convertLocalDateTimeToString(searchTimeRequest.getStartTime()),
                TimeConverter.convertLocalDateTimeToString(searchTimeRequest.getEndTime()));

        for(ErrorInfoDto errorInfo : aiResponse.getCongestionInfo()) {
            System.out.println("실행됨");

            errorInfo.setRelatedOhtId(
                getOhtListInCongestion(
                        searchTimeRequest.getStartTime(), searchTimeRequest.getEndTime(),errorInfo.getPath()
                )
            );
        }

        return AnalyticsToClientResponse.of(aiResponse, searchTimeRequest);
    }

    /** 에러발생시간에 같은 path에 존재하는 oht
     * @author 최서현
     */
    public List<Long> getOhtListInCongestion(LocalDateTime startTime,
                                             LocalDateTime endTime,
                                             String path) throws IOException {

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("path.keyword", path));
        // 필요한 필드만 가져오기
        TermsAggregationBuilder aggregation = AggregationBuilders
                .terms("unique_oht_ids")
                .field("oht_id")
                .size(ohtCnt); // size 파라미터를 통해 반환할 최대 unique id의 수 설정


        SearchResponse searchResponse = elasticsearchQueryUtil.sendEsQuery(startTime, endTime, boolQuery, aggregation);
        return searchResponse.getAggregations().<Terms>get("unique_oht_ids").getBuckets().stream()
                .map(bucket -> ((Number) bucket.getKey()).longValue())
                .collect(Collectors.toList());
    }
}

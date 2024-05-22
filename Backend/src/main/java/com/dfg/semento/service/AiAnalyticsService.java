package com.dfg.semento.service;

import com.dfg.semento.config.OpenFeignConfig;
import com.dfg.semento.dto.request.SearchTimeRequest;
import com.dfg.semento.dto.response.AnalyticsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ai-server-service", url="${ai.server.url}", configuration = OpenFeignConfig.class)
public interface AiAnalyticsService {
    
    //시간관점 모델
    @GetMapping("/ai/analytics/time")
    AnalyticsResponse getAnalyticsResultByTimeAi(@RequestParam("start_date") String startDate,
                                             @RequestParam("end_date") String endDate);

    //path관점 모델
    @GetMapping("/ai/analytics/path")
    AnalyticsResponse getAnalyticsResultByPathAi(@RequestParam("start_date") String startDate,
                                             @RequestParam("end_date") String endDate);
}

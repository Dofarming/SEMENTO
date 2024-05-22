package com.dfg.semento.controller;

import com.dfg.semento.dto.request.SearchTimeRequest;
import com.dfg.semento.dto.response.AnalyticsResponse;
import com.dfg.semento.dto.response.AnalyticsToClientResponse;
import com.dfg.semento.service.AnalyticsService;
import com.dfg.semento.util.TimeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
@Slf4j
public class AiAnalyticsController {

    private final AnalyticsService analyticsService;

    @PostMapping("/ai-detection")
    public AnalyticsToClientResponse aiDetection(@RequestBody SearchTimeRequest searchTimeRequest) throws IOException {
        log.debug("[request] : "+searchTimeRequest.toString());
        AnalyticsToClientResponse response = analyticsService.getAnalytics(searchTimeRequest);
        return response;
    }
}

package com.dfg.semento.controller;

import com.dfg.semento.document.LogDocument;
import com.dfg.semento.dto.request.SearchTimeRequest;
import com.dfg.semento.dto.response.JobResultAnalysisResponse;
import com.dfg.semento.dto.response.OhtJobAnalysisResponse;
import com.dfg.semento.dto.response.OhtJobHourlyResponse;
import com.dfg.semento.dto.response.StateAnalysisResponse;
import com.dfg.semento.dto.response.StateHourlyAnalysisResponse;
import com.dfg.semento.dto.response.StateHourlyResponse;
import com.dfg.semento.service.DashboardService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @PostMapping ("/oht-job-analysis")
    public ResponseEntity<OhtJobAnalysisResponse> ohtJobAnalysis(@Valid @RequestBody SearchTimeRequest request) throws IOException {
        OhtJobAnalysisResponse ohtJobAnalysis = dashboardService.ohtJobAnalysis(request.getStartTime(), request.getEndTime());
        return ResponseEntity.ok(ohtJobAnalysis);
    }

    @PostMapping("/oht-job-hourly")
    public ResponseEntity<List<OhtJobHourlyResponse>> ohtJobHourly(@Valid @RequestBody SearchTimeRequest request) throws
        IOException {
        List<OhtJobHourlyResponse> ohtJobHourlyResponse = dashboardService.ohtJobHourly(request.getStartTime(), request.getEndTime());
        return ResponseEntity.ok(ohtJobHourlyResponse);
    }

    @PostMapping("/job-result-analysis")
    public ResponseEntity<JobResultAnalysisResponse> jobResultAnalysis(@Valid @RequestBody SearchTimeRequest request) throws
        IOException {
        JobResultAnalysisResponse response = dashboardService.jobResultAnalysis(request.getStartTime(), request.getEndTime());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/state-analysis")
    public ResponseEntity<StateAnalysisResponse> stateAnalysis(@Valid @RequestBody SearchTimeRequest request) throws
        IOException {
        StateAnalysisResponse response = dashboardService.stateAnalysis(request.getStartTime(), request.getEndTime());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/state-hourly-analysis")
    public ResponseEntity<StateHourlyAnalysisResponse> stateHourlyAnalysis(@Valid @RequestBody SearchTimeRequest request) throws
        IOException {
        StateHourlyAnalysisResponse response = dashboardService.stateHourlyAnalysis(request.getStartTime(), request.getEndTime());
        return ResponseEntity.ok(response);
    }
}

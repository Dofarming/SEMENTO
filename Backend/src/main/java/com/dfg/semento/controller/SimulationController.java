package com.dfg.semento.controller;

import com.dfg.semento.dto.request.DateAndOhtRequest;
import com.dfg.semento.dto.request.SimulationRequest;
import com.dfg.semento.dto.response.ClassificationLogResponse;
import com.dfg.semento.dto.response.ComparedDataResponse;
import com.dfg.semento.dto.response.ComparedWorkPerTimeResponse;
import com.dfg.semento.dto.response.SimulationLogResponse;
import com.dfg.semento.service.SimulationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping("/simulation")
@RequiredArgsConstructor
@Slf4j
public class SimulationController {
    private final SimulationService simulationService;


    /** 기타 개별 OHT 정보(작업량, 데드라인, 속도, 에러)
     * @author 최서현
     */
    @PostMapping("/work-information")
    public ResponseEntity<ComparedDataResponse> workInfomation(@Valid @RequestBody DateAndOhtRequest request) throws IOException {
        log.debug("[request] : "+request.toString());
        return ResponseEntity.ok(simulationService.getComparedDate(request));
    }

    /** 시간대별 작업량평균 비교
     * @author 최서현
     */
    @PostMapping("/compare-work")
    public ResponseEntity<ComparedWorkPerTimeResponse> compareWork(@Valid @RequestBody DateAndOhtRequest request) throws IOException {
        log.debug("[request] : "+request.toString());
        return ResponseEntity.ok(simulationService.getCompareWork(request));
    }

    /** 작업단위로 조회
     * @author 최서현
     */
    @PostMapping("/classification-log")
    public ResponseEntity<ClassificationLogResponse> classificationLog(@Valid @RequestBody DateAndOhtRequest request) throws IOException {
        log.debug("[request] : "+request.toString());
        return ResponseEntity.ok(simulationService.getClassificationLog(request));
    }


    /** OHT 시뮬레이션 정보 조회
     * @author 최서현
     */
    @PostMapping("/simulation-log")
    public ResponseEntity<SimulationLogResponse> simulationLog(@Valid @RequestBody SimulationRequest request) throws IOException {
        log.debug("[request] : "+request.toString());
        return ResponseEntity.ok(simulationService.getSimulationLog(request));
    }
}

package com.dfg.semento.controller;

import com.dfg.semento.dto.request.WorkInformationRequest;
import com.dfg.semento.service.SimulationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public void workInfomation(@Valid @RequestBody WorkInformationRequest request){
        log.debug("[request] : "+request.toString());
        try{
            simulationService.getAverageSpeedComparison(request.getStartDate(), request.getEndDate(), request.getOhtId());
        } catch(IOException e){
            log.error(e.getMessage());
        }
    }
}

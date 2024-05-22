package com.dfg.semento.dto.response;

import com.dfg.semento.dto.AnalyseResultDto;
import com.dfg.semento.dto.DetectionResultInfoDto;
import com.dfg.semento.dto.ErrorInfoDto;
import com.dfg.semento.dto.request.SearchTimeRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Builder
@ToString
public class AnalyticsToClientResponse {
    private List<DetectionResultInfoDto> detectionResult;
    public static AnalyticsToClientResponse of(AnalyticsResponse analyticsResponse, SearchTimeRequest timeRequest) {
        List<DetectionResultInfoDto> detectionResult = new ArrayList<>();

        for(ErrorInfoDto errorInfo : analyticsResponse.getCongestionInfo()){
            //원인 하나만 판별하기
            AnalyseResultDto analyseResults = errorInfo.getAnalyseResult();
            Map<String, Double> mainCause = analyseResults.calculatedMainCause();
            Map.Entry<String, Double> entry = mainCause.entrySet().iterator().next();

            // 에러 시작 시간 계산: startDate로부터 errorInfo.getTime() 초 후
            LocalDateTime resultStartTime = timeRequest.getStartTime().plusSeconds(errorInfo.getTime());
            // 에러 종료 시간 계산: resultStartTime으로부터 errorInfo.getCongestionTime() 초 후
            LocalDateTime resultEndTime = resultStartTime.plusSeconds(errorInfo.getCongestionTime());



            DetectionResultInfoDto dto = DetectionResultInfoDto.builder()
                                                                .no(detectionResult.size()+1)
                                                                .path(errorInfo.getPath())
                                                                .cause(entry.getKey())
                                                                .accuracy(entry.getValue())
                                                                .causeOht(errorInfo.getOhtId())
                                                                .startDate(resultStartTime)
                                                                .endDate(resultEndTime)
                                                                .currentNode(errorInfo.getCurrentNode())
                                                                .nextNode(errorInfo.getNextNode())
                                                                .ohtId(errorInfo.getRelatedOhtId())
                                                                .build();
            detectionResult.add(dto);
        }

        return AnalyticsToClientResponse.builder()
                .detectionResult(detectionResult)
                .build();
    }

}

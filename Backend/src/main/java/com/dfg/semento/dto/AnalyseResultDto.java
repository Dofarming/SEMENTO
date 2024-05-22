package com.dfg.semento.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
@ToString
public class AnalyseResultDto {
    @SerializedName("etc-error-probability")
    private double etcErrorProbability;
    @SerializedName("facility-error-probability")
    private double facilityErrorProbability;
    @SerializedName("oht-error-probability")
    private double ohtErrorProbability;

    public Map<String, Double> calculatedMainCause(){
        // 백분율로 변환
        double etcErrorPercent = etcErrorProbability * 100;
        double facilityErrorPercent = facilityErrorProbability * 100;
        double ohtErrorPercent = ohtErrorProbability * 100;

        String highestCause = "E";
        double highestProbability = etcErrorPercent;

        if (facilityErrorPercent > highestProbability) {
            highestCause = "F";
            highestProbability = facilityErrorPercent;
        }

        if (ohtErrorPercent > highestProbability) {
            highestCause = "O";
            highestProbability = ohtErrorPercent;
        }

        Map<String, Double> result = new HashMap<>();
        result.put(highestCause, highestProbability);
        return result;
    }
}

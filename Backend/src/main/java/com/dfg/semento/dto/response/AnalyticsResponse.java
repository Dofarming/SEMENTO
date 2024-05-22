package com.dfg.semento.dto.response;

import com.dfg.semento.dto.AnalyseResultDto;
import com.dfg.semento.dto.ErrorInfoDto;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class AnalyticsResponse {
    @SerializedName("congestion-info")
    private List<ErrorInfoDto> congestionInfo;
}

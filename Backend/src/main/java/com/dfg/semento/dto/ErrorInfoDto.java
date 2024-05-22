package com.dfg.semento.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
@Setter
public class ErrorInfoDto {
    @SerializedName("oht-id")
    private Long ohtId;
    private List<Long> relatedOhtId;
    @SerializedName("time")
    private Long time;
    @SerializedName("congestion-time")
    private Long congestionTime;
    @SerializedName("path")
    private String path;
    @SerializedName("error-code")
    private int errorCode;
    @SerializedName("status")
    private String status;
    @SerializedName("carrier")
    private boolean carrier;
    @SerializedName("error")
    private int error;
    @SerializedName("speed")
    private double speed;
    @SerializedName("is-fail")
    private boolean isFail;
    @SerializedName("current-node")
    private String currentNode;
    @SerializedName("next-node")
    private String nextNode;
    @SerializedName("analyse-result")
    private AnalyseResultDto analyseResult;
}

package com.dfg.semento.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class LocationDto {
    private String path;
    private String currNode;
    private double pointX;
    private double pointY;
}

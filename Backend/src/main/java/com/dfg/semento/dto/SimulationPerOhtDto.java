package com.dfg.semento.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SimulationPerOhtDto {
    private Long ohtId;
    private LocationDto location;
    private String status;
    private boolean carrier;
    private int error;
    private double speed;
    private boolean isFail;
}

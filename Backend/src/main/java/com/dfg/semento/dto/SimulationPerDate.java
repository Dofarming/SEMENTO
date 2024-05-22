package com.dfg.semento.dto;

import com.dfg.semento.dto.response.SimulationLogResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@ToString
public class SimulationPerDate {
    private LocalDateTime time;
    private List<SimulationPerOhtDto> data;
}

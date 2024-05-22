package com.dfg.semento.dto.response;

import com.dfg.semento.dto.SimulationPerDate;
import com.dfg.semento.dto.SimulationPerOhtDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@ToString
public class SimulationLogResponse {
    private List<SimulationPerDate> simulationLog;
}

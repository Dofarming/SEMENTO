package com.dfg.semento.dto.request;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SimulationRequest {
    @NotNull
    private List<Long> ohtId;
    @NotNull
    //@PastOrPresent
    private LocalDateTime startDate;
    @NotNull
    //@PastOrPresent
    private LocalDateTime endDate;
}

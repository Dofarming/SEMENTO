package com.dfg.semento.dto.response;

import com.dfg.semento.dto.WorkPerTime;
import lombok.*;

import java.util.List;

@ToString
@Getter @Setter
@AllArgsConstructor
@Builder
public class ComparedWorkPerTimeResponse {
    private List<WorkPerTime> workPerTime;
}

package com.dfg.semento.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class WorkPerTime {
    private LocalDateTime time;
    private int me;
    private int average;
}

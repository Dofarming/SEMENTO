package com.dfg.semento.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
@Builder
public class LogPerWork {
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public Long ohtId;
    public List<Integer> errors;
    public double averageSpeed;
    public boolean outOfDeadline;
}

package com.dfg.semento.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@ToString
public class DetectionResultInfoDto {
    private int no;
    private String  path;
    private String cause;
    private double accuracy;
    private Long causeOht;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String currentNode;
    private String nextNode;
    private List<Long> ohtId;
}

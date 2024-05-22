package com.dfg.semento.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoubleDataDto {
    private double data;
    private double percent;
}

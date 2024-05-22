package com.dfg.semento.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DateAndOhtRequest {

    //검색조건 시작일자
    @NotNull
    //@PastOrPresent
    private LocalDateTime startDate;

    //검색조건 종료일자
    @NotNull
    //@PastOrPresent
    private LocalDateTime endDate;

    //각 OHT의 ID
    @NotNull
    private Long ohtId;
}

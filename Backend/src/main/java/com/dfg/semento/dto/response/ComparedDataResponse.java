package com.dfg.semento.dto.response;

import com.dfg.semento.dto.DoubleDataDto;
import com.dfg.semento.dto.IntegerDataDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ComparedDataResponse {
    private IntegerDataDto totalWork;

    private IntegerDataDto outOfDeadLine;

    private DoubleDataDto averageSpeed;

    private IntegerDataDto ohtError;
}

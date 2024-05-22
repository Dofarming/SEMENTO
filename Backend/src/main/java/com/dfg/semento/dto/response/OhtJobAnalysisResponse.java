package com.dfg.semento.dto.response;

import com.dfg.semento.dto.DoubleDataDto;
import com.dfg.semento.dto.IntegerDataDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OhtJobAnalysisResponse {

	private IntegerDataDto ohtCount;

	private IntegerDataDto totalWork;

	private DoubleDataDto averageWork;
}

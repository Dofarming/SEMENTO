package com.dfg.semento.dto.response;

import com.dfg.semento.dto.IntegerDataDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StateAnalysisResponse {

	private IntegerDataDto deadline;

	private IntegerDataDto averageWorkTime;

	private IntegerDataDto averageIdleTime;
}

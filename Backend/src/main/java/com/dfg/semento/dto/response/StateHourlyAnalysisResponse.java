package com.dfg.semento.dto.response;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StateHourlyAnalysisResponse {

	private List<StateHourlyResponse> workHourCount;

	private List<StateHourlyResponse> idleHourCount;

	private int maxJobTime;

	private int maxWorkTime;

	private int maxIdleTime;

}

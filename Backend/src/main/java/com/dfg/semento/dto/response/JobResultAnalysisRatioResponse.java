package com.dfg.semento.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class JobResultAnalysisRatioResponse {

	private int totalWork;

	private int successWork;

	private int failedWork;

	private double successPercentage;

	private double failedPercentage;
}

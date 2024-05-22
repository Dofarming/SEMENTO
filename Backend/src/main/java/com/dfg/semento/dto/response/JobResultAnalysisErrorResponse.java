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
public class JobResultAnalysisErrorResponse {

	private int totalError;

	private int ohtError;

	private int facilityError;

	private double ohtErrorPercentage;

	private double facilityErrorPercentage;
}

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
public class JobResultAnalysisResponse {

	JobResultAnalysisRatioResponse jobResultRatio;

	JobResultAnalysisErrorResponse jobResultError;

	List<JobResultAnalysisErrorLogResponse> jobResultErrorLog;
}

package com.dfg.semento.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SearchTimeRequest {

	@NotNull
	private LocalDateTime startTime;

	@NotNull
	private LocalDateTime endTime;

}

package com.dfg.semento.dto.response;

import com.dfg.semento.dto.LogPerWork;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class ClassificationLogResponse {
    public int totalCnt;
    public List<LogPerWork> logPerWork;
}

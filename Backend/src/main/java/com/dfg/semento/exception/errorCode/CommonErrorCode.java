package com.dfg.semento.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode{
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid parameter included"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not exists"),
    RESULT_NOT_FOUND(HttpStatus.NOT_FOUND, "Result Not Found"),
    NO_RUNNING_OHT(HttpStatus.NOT_FOUND, "Running OHT Not Found");

    private final HttpStatus httpStatus;
    private final String message;
}

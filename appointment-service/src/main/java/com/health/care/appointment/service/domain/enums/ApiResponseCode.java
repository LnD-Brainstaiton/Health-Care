package com.health.care.appointment.service.domain.enums;

import com.health.care.appointment.service.domain.common.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public enum ApiResponseCode {

    OPERATION_SUCCESSFUL("S100000"),
    RECORD_NOT_FOUND("E000101"),
    INVALID_REQUEST_DATA("E000102"),
    SERVICE_UNAVAILABLE("E000103"),
    DB_OPERATION_FAILED("E000010"),
    OPERATION_UNSUCCESSFUL("E00214"),
    INTER_SERVICE_COMMUNICATION_ERROR("E000111"),
    REQUEST_PROCESSING_FAILED("E000106"),
    ;
    private String responseCode;


    public static boolean isOperationSuccessful(ApiResponse apiResponse) {
        return Objects.nonNull(apiResponse) && apiResponse.getResponseCode().equals(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode());
    }

    public static boolean isNotOperationSuccessful(ApiResponse apiResponse) {
        return !isOperationSuccessful(apiResponse);
    }
}
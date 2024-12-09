package com.healthcare.tfaservice.domain.enums;

import com.healthcare.tfaservice.domain.common.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public enum ApiResponseCode {

    OPERATION_SUCCESSFUL("S100000"),
    RECORD_NOT_FOUND("TFA000101"),
    INVALID_REQUEST_DATA("TFA000102"),
    SERVICE_UNAVAILABLE("TFA000103"),
    DB_OPERATION_FAILED("TFA000010"),
    OPERATION_UNSUCCESSFUL("TFA00214"),
    INTER_SERVICE_COMMUNICATION_ERROR("TFA000111"),
    REQUEST_PROCESSING_FAILED("TFA000106"),
    ;
    private String responseCode;

    public static boolean isOperationSuccessful(ApiResponse apiResponse) {
        return Objects.nonNull(apiResponse) && apiResponse.getResponseCode().equals(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode());
    }

    public static boolean isNotOperationSuccessful(ApiResponse apiResponse) {
        return !isOperationSuccessful(apiResponse);
    }
}

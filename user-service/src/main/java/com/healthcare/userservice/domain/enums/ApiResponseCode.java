package com.healthcare.userservice.domain.enums;

import com.healthcare.userservice.domain.common.ApiResponse;
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
    NO_ACCOUNT_FOUND("E000104"),
    DB_OPERATION_FAILED("E000010"),
    OPERATION_UNSUCCESSFUL("E00214"),
    INTER_SERVICE_COMMUNICATION_ERROR("E000111"),
    REQUEST_PROCESSING_FAILED("E000106"),
    AUTHENTICATION_FAILED("E000401"),
    INVALID_PASSWORD("E000107"),
    ;
    private String responseCode;

    public static boolean isOperationSuccessful(ApiResponse apiResponse) {
        return Objects.nonNull(apiResponse) && apiResponse.getResponseCode().equals(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode());
    }

    public static boolean isNotOperationSuccessful(ApiResponse apiResponse) {
        return !isOperationSuccessful(apiResponse);
    }
}

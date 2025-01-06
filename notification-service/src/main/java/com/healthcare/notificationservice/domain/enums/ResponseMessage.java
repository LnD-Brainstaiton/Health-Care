package com.healthcare.notificationservice.domain.enums;

import com.healthcare.notificationservice.common.utils.MessageSourceProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseMessage {

    OPERATION_SUCCESSFUL(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode(), "operation.success"),
    SERVICE_UNAVAILABLE(ApiResponseCode.SERVICE_UNAVAILABLE.getResponseCode(), "service.unavailable"),
    RECORD_NOT_FOUND(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode(), "record.not.found"),
    INVALID_REQUEST_DATA(ApiResponseCode.INVALID_REQUEST_DATA.getResponseCode(), "invalid.request.data"),
    INTER_SERVICE_COMMUNICATION_ERROR(ApiResponseCode.INTER_SERVICE_COMMUNICATION_ERROR.getResponseCode(), "inter.service.communication.exception"),
    INTERNAL_SERVICE_EXCEPTION(ApiResponseCode.REQUEST_PROCESSING_FAILED.getResponseCode(), "internal.service.exception"),
    DATABASE_EXCEPTION(ApiResponseCode.DB_OPERATION_FAILED.getResponseCode(), "database.exception"),
    RECORD_NOT_MATCHED(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode(), "record.not.matched"),
    RECORD_ALREADY_EXIST(ApiResponseCode.INVALID_REQUEST_DATA.getResponseCode(), "record.already.exist"),
    TFA_CONFIGURATION_NOT_FOUND_EXCEPTION(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode(), "tfa.configuration.not.found"),
    TRY_LATER_WITH_TIME(ApiResponseCode.INVALID_REQUEST_DATA.getResponseCode(), "try.later.with.time"),
    OTP_EXPIRED(ApiResponseCode.INVALID_REQUEST_DATA.getResponseCode(), "otp.expired");

    private String responseCode;
    private String responseMessage;

    public String getLocalizedMessage() {
        return MessageSourceProvider.getMessage(this.responseMessage);
    }
}

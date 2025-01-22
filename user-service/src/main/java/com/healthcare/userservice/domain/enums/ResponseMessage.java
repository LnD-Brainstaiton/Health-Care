package com.healthcare.userservice.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseMessage {

    OPERATION_SUCCESSFUL(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode(), "operation.success"),
    SERVICE_UNAVAILABLE(ApiResponseCode.SERVICE_UNAVAILABLE.getResponseCode(), "service.unavailable"),
    RECORD_NOT_FOUND(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode(), "record.not.found"),
    NO_ACCOUNT_FOUND(ApiResponseCode.NO_ACCOUNT_FOUND.getResponseCode(), "no account.found"),
    INVALID_REQUEST_DATA(ApiResponseCode.INVALID_REQUEST_DATA.getResponseCode(), "invalid.request.data"),
    INTER_SERVICE_COMMUNICATION_ERROR(ApiResponseCode.INTER_SERVICE_COMMUNICATION_ERROR.getResponseCode(), "inter.service.communication.exception"),
    INTERNAL_SERVICE_EXCEPTION(ApiResponseCode.REQUEST_PROCESSING_FAILED.getResponseCode(), "internal.service.exception"),
    DATABASE_EXCEPTION(ApiResponseCode.DB_OPERATION_FAILED.getResponseCode(), "database.exception"),
    RECORD_NOT_MATCHED(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode(), "record.not.matched"),
    RECORD_ALREADY_EXIST(ApiResponseCode.INVALID_REQUEST_DATA.getResponseCode(), "record.already.exist"),
    AUTHENTICATION_FAILED(ApiResponseCode.AUTHENTICATION_FAILED.getResponseCode(), "authentication.failed.due.to.bad.credentials"),
    INVALID_MOBILE_NUMBER(ApiResponseCode.INVALID_REQUEST_DATA.getResponseCode(), "Mobile number is already registered"),
    REQUEST_CAN_NOT_PROCESS(ApiResponseCode.INVALID_REQUEST_DATA.getResponseCode(),"REQUEST CAN NOT PROCESS"),
    ALREADY_REQUEST_PENDING(ApiResponseCode.INVALID_REQUEST_DATA.getResponseCode(),"ALREADY REQUEST PENDING"),
    EMAIL_ALREADY_EXISTS(ApiResponseCode.INVALID_REQUEST_DATA.getResponseCode(), "EMAIL ALREADY EXISTS"),
    MOBILE_ALREADY_EXISTS(ApiResponseCode.INVALID_REQUEST_DATA.getResponseCode(), "MOBILE ALREADY EXISTS"),
    INVALID_JSON_BODY(ApiResponseCode.INVALID_REQUEST_DATA.getResponseCode(), "Invalid JSON structure: not an object"),
    OLD_PASSWORD_NOT_VALID(ApiResponseCode.INVALID_PASSWORD.getResponseCode(), "old.password.does.not.match"),
    NEW_PASSWORD_MISMATCH(ApiResponseCode.INVALID_PASSWORD.getResponseCode(), "new.password.mismatch"),
    COOKIE_NOT_FOUND(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode(), "cookie 'bmdckyc_csrf_cookie' not found"),
    FAIL_BMDC_API_CALL(ApiResponseCode.INTER_SERVICE_COMMUNICATION_ERROR.getResponseCode() , "failed to connect to BMDC API server"),
    BMDC_Authentication_Failure(ApiResponseCode.AUTHENTICATION_FAILED.getResponseCode(), "Authentication failed due to invalid Cookie"),
    ;

    private String responseCode;
    private String responseMessage;
}

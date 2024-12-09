package com.health.care.appointment.service.common.utils;

import com.health.care.appointment.service.domain.common.ApiResponse;
import com.health.care.appointment.service.domain.enums.ApiResponseCode;
import com.health.care.appointment.service.domain.enums.ResponseMessage;

import java.util.Objects;

public class ResponseUtils {

    public static <T> ApiResponse<T> createResponseObject(String message, T data) {
        ApiResponse apiResponse = new ApiResponse<T>();
        apiResponse.setResponseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode());
        apiResponse.setResponseMessage(message);
        apiResponse.setData(data);
        return apiResponse;
    }

    public static <T> ApiResponse<T> createResponseObject(String code, String message) {
        ApiResponse apiResponse = new ApiResponse<T>();
        apiResponse.setResponseCode(code);
        apiResponse.setResponseMessage(message);
        return apiResponse;
    }

    public static <T> ApiResponse<T> createResponseObject(ResponseMessage responseMessage, T data) {
        ApiResponse apiResponse = new ApiResponse<T>();
        apiResponse.setResponseCode(responseMessage.getResponseCode());
        apiResponse.setResponseMessage(responseMessage.getResponseMessage());
        apiResponse.setData(data);
        return apiResponse;
    }

    public static <T> ApiResponse<T> createResponseObject(ResponseMessage responseMessage) {
        ApiResponse apiResponse = new ApiResponse<T>();
        apiResponse.setResponseCode(responseMessage.getResponseCode());
        apiResponse.setResponseMessage(responseMessage.getResponseMessage());
        return apiResponse;
    }

    public static <T> ApiResponse<T> createSuccessfulResponse() {
        ApiResponse apiResponse = new ApiResponse<T>();
        apiResponse.setResponseCode(ResponseMessage.OPERATION_SUCCESSFUL.getResponseCode());
        apiResponse.setResponseMessage("Operation is completed successfully.");
        return apiResponse;
    }

    public static <T> ApiResponse<T> copyResponse(ApiResponse<T> apiResponse, ApiResponse sourceResponse, Class<T> clazz) {
        apiResponse = Objects.isNull(apiResponse) ? new ApiResponse<>() : apiResponse;
        apiResponse.setResponseCode(sourceResponse.getResponseCode());
        apiResponse.setResponseMessage(sourceResponse.getResponseMessage());
        if (Objects.nonNull(sourceResponse.getData())
                && Objects.nonNull(clazz) && clazz.isInstance(sourceResponse.getData())) {
            apiResponse.setData(clazz.cast(sourceResponse.getData()));
        }
        return apiResponse;
    }

    public static <T> ApiResponse<T> copyResponse(ApiResponse<T> apiResponse, ApiResponse sourceResponse) {
        return copyResponse(apiResponse, sourceResponse, null);
    }

    public static <T> ApiResponse<T> createResponseObject(String message) {
        ApiResponse apiResponse = new ApiResponse<T>();
        apiResponse.setResponseCode(ResponseMessage.OPERATION_SUCCESSFUL.getResponseCode());
        apiResponse.setResponseMessage(message);
        return apiResponse;
    }

}

package com.healthcare.notificationservice.common.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.notificationservice.domain.common.ApiResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public abstract class BaseExceptionHandler extends ResponseEntityExceptionHandler {

    protected static final Logger errorLogger = LoggerFactory.getLogger("errorLogger");
    protected static final ObjectMapper objectMapper = new ObjectMapper();

    protected ApiResponse<Void> buildApiResponse(String messageCode, String message) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setResponseMessage(message);
        apiResponse.setResponseCode(messageCode);
        return apiResponse;
    }

    protected ApiResponse<Object> buildApiResponse(String messageCode, String message, Object data) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setResponseMessage(message);
        apiResponse.setResponseCode(messageCode);
        apiResponse.setData(data);
        return apiResponse;
    }

    protected String getMessageContent(String bodyContent) {
        try {
            ApiResponse apiResponse = objectMapper.readValue(bodyContent, new TypeReference<ApiResponse<?>>() {
            });
            return apiResponse.getResponseMessage();
        } catch (Exception ex) {
            errorLogger.error(ex.getLocalizedMessage(), ex);
        }
        return StringUtils.isBlank(bodyContent) ? Strings.EMPTY : bodyContent;
    }

    protected String processFeignExceptionMessage(int status, String messageContent) {
        if(status<0)
            return "Service Unavailable";
        else if (status == HttpStatus.OK.value())
            return "Payload/data conversion exception";
        else if (status >= HttpStatus.INTERNAL_SERVER_ERROR.value())
            return "Inter Service communication exception!";

        return getMessageContent(messageContent);
    }

    protected <T> String serializeObject(T object) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            errorLogger.error(e.getMessage(), e);
        }
        return StringUtils.EMPTY;
    }

}

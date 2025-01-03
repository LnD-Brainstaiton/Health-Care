package com.health_care.user_service.common.exceptions;

import com.health_care.user_service.domain.enums.ResponseMessage;

public class RequestCanNotProcessException extends PreValidationException {
    public RequestCanNotProcessException(ResponseMessage responseMessage) {
        super(responseMessage);
    }
}

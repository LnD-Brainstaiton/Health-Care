package com.health_care.user_service.common.exceptions;

import com.health_care.user_service.domain.enums.ResponseMessage;

public class UnauthorizedResourceException extends PreValidationException {
    public UnauthorizedResourceException(ResponseMessage responseMessage) {
        super(responseMessage);
    }
}

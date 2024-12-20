package com.health_care.user_service.common.exceptions;

import com.health_care.user_service.domain.enums.ResponseMessage;

public class AlreadyExistsException extends PreValidationException {
    public AlreadyExistsException(ResponseMessage responseMessage) {
        super(responseMessage);
    }
}

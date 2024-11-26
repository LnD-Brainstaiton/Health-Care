package com.health_care.user_service.common.exceptions;

import com.health_care.user_service.domain.enums.ResponseMessage;

public abstract class PreValidationException extends CustomRootException {
    public PreValidationException(ResponseMessage responseMessage) {
        super(responseMessage);
    }

    public PreValidationException(String messageCode, String messageKey) {
        super(messageCode, messageKey);
    }
}

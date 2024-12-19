package com.healthcare.userservice.common.exceptions;

import com.healthcare.userservice.domain.enums.ResponseMessage;

public abstract class PreValidationException extends CustomRootException {
    public PreValidationException(ResponseMessage responseMessage) {
        super(responseMessage);
    }

    public PreValidationException(String messageCode, String messageKey) {
        super(messageCode, messageKey);
    }
}

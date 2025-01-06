package com.healthcare.userservice.common.exceptions;

import com.healthcare.userservice.domain.enums.ResponseMessage;

public class UnauthorizedResourceException extends PreValidationException {
    public UnauthorizedResourceException(ResponseMessage responseMessage) {
        super(responseMessage);
    }
}

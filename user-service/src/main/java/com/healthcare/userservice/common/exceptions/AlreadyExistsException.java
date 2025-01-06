package com.healthcare.userservice.common.exceptions;

import com.healthcare.userservice.domain.enums.ResponseMessage;

public class AlreadyExistsException extends PreValidationException {
    public AlreadyExistsException(ResponseMessage responseMessage) {
        super(responseMessage);
    }
}

package com.healthcare.userservice.common.exceptions;

import com.healthcare.userservice.domain.enums.ResponseMessage;

public class RequestCanNotProcessException extends PreValidationException {
    public RequestCanNotProcessException(ResponseMessage responseMessage) {
        super(responseMessage);
    }
}

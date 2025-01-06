package com.healthcare.userservice.common.exceptions;

import com.healthcare.userservice.domain.enums.ResponseMessage;

public class InvalidRequestDataException extends PreValidationException {

    public InvalidRequestDataException(ResponseMessage message) {
        super(message);
    }

    public InvalidRequestDataException(String messageCode, String messageKey) {
        super(messageCode, messageKey);
    }


}

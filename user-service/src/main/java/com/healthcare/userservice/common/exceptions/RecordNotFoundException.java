package com.healthcare.userservice.common.exceptions;

import com.healthcare.userservice.domain.enums.ResponseMessage;

public class RecordNotFoundException extends CustomRootException {

    public RecordNotFoundException(ResponseMessage responseMessage) {
        super(responseMessage);
    }

    public RecordNotFoundException(String messageCode, String messageKey) {
        super(messageCode, messageKey);
    }

    public RecordNotFoundException(String message) {
        super(message);
    }
}

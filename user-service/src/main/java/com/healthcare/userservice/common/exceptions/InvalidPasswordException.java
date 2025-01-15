package com.healthcare.userservice.common.exceptions;

import com.healthcare.userservice.domain.enums.ResponseMessage;

public class InvalidPasswordException extends CustomRootException{


    public InvalidPasswordException(ResponseMessage message) {
        super(message);
    }

    public InvalidPasswordException(String messageCode, String messageKey) {
        super(messageCode, messageKey);
    }
}

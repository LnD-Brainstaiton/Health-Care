package com.healthcare.tfaservice.common.exceptions;

import com.healthcare.tfaservice.domain.enums.ResponseMessage;

public class InvalidRequestDataException extends CustomRootException {

    public InvalidRequestDataException(ResponseMessage message) {
        super(message);
    }

    public InvalidRequestDataException(String messageCode, String messageKey) {
        super(messageCode, messageKey);
    }


}

package com.health_care.user_service.common.exceptions;

import com.health_care.user_service.domain.enums.ResponseMessage;

public class InvalidRequestDataException extends PreValidationException {

    public InvalidRequestDataException(ResponseMessage message) {
        super(message);
    }

    public InvalidRequestDataException(String messageCode, String messageKey) {
        super(messageCode, messageKey);
    }


}

package com.health.care.appointment.service.common.exceptions;

import com.health.care.appointment.service.domain.enums.ResponseMessage;

public class InvalidRequestDataException extends PreValidationException {

    public InvalidRequestDataException(ResponseMessage message) {
        super(message);
    }

    public InvalidRequestDataException(String messageCode, String messageKey) {
        super(messageCode, messageKey);
    }


}
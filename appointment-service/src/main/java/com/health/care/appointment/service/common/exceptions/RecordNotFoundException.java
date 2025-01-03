package com.health.care.appointment.service.common.exceptions;

import com.health.care.appointment.service.domain.enums.ResponseMessage;

public class RecordNotFoundException extends CustomRootException{

    public RecordNotFoundException(ResponseMessage message) {
        super(message);
    }

    public RecordNotFoundException(String messageCode, String messageKey) {
        super(messageCode, messageKey);
    }
}

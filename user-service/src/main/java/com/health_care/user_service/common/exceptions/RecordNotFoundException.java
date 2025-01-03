package com.health_care.user_service.common.exceptions;

import com.health_care.user_service.domain.enums.ResponseMessage;

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

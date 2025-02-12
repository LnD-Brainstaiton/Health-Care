package com.healthcare.notificationservice.common.exceptions;

import com.healthcare.notificationservice.domain.enums.ResponseMessage;

public class RecordNotFoundException extends CustomRootException {


    public RecordNotFoundException(ResponseMessage message) {
        super(message);
    }

    public RecordNotFoundException(String messageCode, String messageKey) {
        super(messageCode, messageKey);
    }


}

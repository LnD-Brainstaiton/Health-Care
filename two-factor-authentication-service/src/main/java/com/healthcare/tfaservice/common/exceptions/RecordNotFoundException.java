package com.healthcare.tfaservice.common.exceptions;

import com.healthcare.tfaservice.domain.enums.ResponseMessage;

public class RecordNotFoundException extends CustomRootException {

    private static final String MESSAGE_CODE = "TFA450";

    public RecordNotFoundException(ResponseMessage message) {
        super(message);
    }

    public RecordNotFoundException(String messageCode, String messageKey) {
        super(messageCode, messageKey);
    }


}

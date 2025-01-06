package com.healthcare.notificationservice.common.exceptions;

import com.healthcare.notificationservice.domain.enums.ResponseMessage;
import com.healthcare.notificationservice.common.utils.MessageSourceProvider;

public abstract class CustomRootException extends RuntimeException {

    private String messageCode;

    public CustomRootException(ResponseMessage responseMessage) {
        super(MessageSourceProvider.getMessage(responseMessage.getResponseMessage()));
        this.messageCode = responseMessage.getResponseCode();
    }


    public CustomRootException(String messageCode, String messageKey) {
        super(MessageSourceProvider.getMessage(messageKey));
        this.messageCode = messageCode;
    }

    public CustomRootException(String message) {
        super(message);
    }

    public String getMessageCode() {
        return messageCode;
    }
}

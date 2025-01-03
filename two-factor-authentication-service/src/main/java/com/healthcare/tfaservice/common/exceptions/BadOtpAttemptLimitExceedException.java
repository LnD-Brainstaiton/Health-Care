package com.healthcare.tfaservice.common.exceptions;


import com.healthcare.tfaservice.domain.enums.ResponseMessage;

public class BadOtpAttemptLimitExceedException extends CustomRootException {

    private static final String MESSAGE_CODE = "ETFBAD451";

    public BadOtpAttemptLimitExceedException(ResponseMessage responseMessage) {
        super(MESSAGE_CODE, responseMessage.getResponseMessage());
    }

    public BadOtpAttemptLimitExceedException(String messageKey) {
        super(MESSAGE_CODE, messageKey);
    }
}

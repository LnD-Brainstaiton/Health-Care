package com.health.care.appointment.service.common.exceptions;

import com.health.care.appointment.service.domain.enums.ResponseMessage;

public class FeignClientException extends CustomRootException {
    public FeignClientException(ResponseMessage responseMessage) {
        super(responseMessage);
    }

    public FeignClientException(String messageCode, String messageKey) {
        super(messageCode, messageKey);
    }
}

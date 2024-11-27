package com.health_care.user_service.common.exceptions;

import com.health_care.user_service.domain.enums.ResponseMessage;

public class FeignClientException extends CustomRootException {
    public FeignClientException(ResponseMessage responseMessage) {
        super(responseMessage);
    }

    public FeignClientException(String messageCode, String messageKey) {
        super(messageCode, messageKey);
    }
}

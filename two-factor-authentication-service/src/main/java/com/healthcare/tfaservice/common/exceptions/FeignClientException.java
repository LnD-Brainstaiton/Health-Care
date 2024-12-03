package com.healthcare.tfaservice.common.exceptions;

import com.healthcare.tfaservice.domain.enums.ResponseMessage;

public class FeignClientException extends CustomRootException {
    public FeignClientException(ResponseMessage responseMessage) {
        super(responseMessage);
    }

    public FeignClientException(String messageCode, String messageKey) {
        super(messageCode, messageKey);
    }
}

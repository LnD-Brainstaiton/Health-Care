package com.healthcare.userservice.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BmdcValidationRequest {
    private String captchaCode;
    private int registrationNo;
}

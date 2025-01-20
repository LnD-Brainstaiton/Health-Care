package com.healthcare.userservice.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorRegistrationValidationRequest {

    @JsonProperty("bmdckyc_csrf_token")
    private String csrfToken;

    @JsonProperty("reg_ful_no")
    private int registrationNo;

    @JsonProperty("captcha_code")
    private String captchaCode;

    @JsonProperty("reg_student")
    private int regStudent;

    @JsonProperty("action_key")
    private String actionKey;

    @JsonProperty("action_flag")
    private int actionFlag;
}


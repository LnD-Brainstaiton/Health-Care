package com.healthcare.userservice.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TfaVerifyRequest {

    @NotBlank(message = "username.notblank")
    private String userName;

    @NotBlank(message = "generatedOtp.notblank")
    private String generatedOtp;

    @NotNull(message = "sessionId.notnull")
    private String sessionId;
}

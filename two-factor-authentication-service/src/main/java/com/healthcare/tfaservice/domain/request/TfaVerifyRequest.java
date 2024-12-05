package com.healthcare.tfaservice.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TfaVerifyRequest implements Serializable {

    @NotBlank(message = "Username cannot be empty or contain only whitespace.")
    private String userName;

    @NotBlank(message = "Generated OTP cannot be empty or null.")
    private String generatedOtp;

    @NotBlank(message = "Session ID cannot be empty or null.")
    private String sessionId;


}

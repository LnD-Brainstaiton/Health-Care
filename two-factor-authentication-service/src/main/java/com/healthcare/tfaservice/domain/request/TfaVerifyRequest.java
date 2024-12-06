package com.healthcare.tfaservice.domain.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TfaVerifyRequest implements Serializable {

    @NotBlank(message = "username.notblank")
    private String userName;

    @NotBlank(message = "generatedOtp.notblank")
    private String generatedOtp;

    @NotNull(message = "sessionId.notnull")
    private String sessionId;


}

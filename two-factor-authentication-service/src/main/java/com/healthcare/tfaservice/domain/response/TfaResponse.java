package com.healthcare.tfaservice.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TfaResponse implements Serializable {

    private String generatedOtp;

    private String sessionId;

    private Integer otpValidityInMinute;
}

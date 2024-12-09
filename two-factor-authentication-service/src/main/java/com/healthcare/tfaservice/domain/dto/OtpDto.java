package com.healthcare.tfaservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpDto implements Serializable {
    private String sessionId;
    private String generatedOtp;
}

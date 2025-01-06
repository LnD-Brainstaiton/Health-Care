package com.healthcare.userservice.service.interfaces;

import com.healthcare.userservice.domain.request.OtpVerificationRequest;

public interface IOtpService {
    Boolean generateAndSendOtp(OtpVerificationRequest otpVerificationRequest);
}

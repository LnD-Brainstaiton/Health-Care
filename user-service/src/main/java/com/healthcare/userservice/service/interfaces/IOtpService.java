package com.healthcare.userservice.service.interfaces;

import com.healthcare.userservice.domain.request.OtpVerificationRequest;
import com.healthcare.userservice.domain.request.TfaVerifyRequest;
import com.healthcare.userservice.domain.response.TfaResponse;

public interface IOtpService {
    TfaResponse generateAndSendOtp(OtpVerificationRequest otpVerificationRequest);

    Boolean verifyOtp(TfaVerifyRequest tfaVerifyRequest);
}

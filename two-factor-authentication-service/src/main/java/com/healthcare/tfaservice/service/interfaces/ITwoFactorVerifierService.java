package com.healthcare.tfaservice.service.interfaces;

import com.healthcare.tfaservice.domain.request.TfaVerifyRequest;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface ITwoFactorVerifierService {

    Boolean verifyOtp(TfaVerifyRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException;
}

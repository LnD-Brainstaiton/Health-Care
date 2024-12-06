package com.healthcare.tfaservice.service.interfaces;

import com.healthcare.tfaservice.domain.request.TfaRequest;
import com.healthcare.tfaservice.domain.response.TfaResponse;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface ITwoFactorGeneratorService {

    TfaResponse getOtp(TfaRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException;
}

package com.healthcare.tfaservice.service.interfaces;

import com.healthcare.tfaservice.domain.request.TfaRequest;
import com.healthcare.tfaservice.domain.request.TfaVerifyRequest;
import com.healthcare.tfaservice.domain.response.TfaResponse;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface ITfaService {

    TfaResponse generateOtp(TfaRequest tfaRequest) throws NoSuchAlgorithmException, InvalidKeySpecException;

    Boolean validateOtp(TfaVerifyRequest tfaVerifyRequest) throws NoSuchAlgorithmException, InvalidKeySpecException;
}

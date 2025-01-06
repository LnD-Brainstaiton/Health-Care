package com.healthcare.tfaservice.service.impl;

import com.healthcare.tfaservice.domain.request.TfaRequest;
import com.healthcare.tfaservice.domain.request.TfaVerifyRequest;
import com.healthcare.tfaservice.domain.response.TfaResponse;
import com.healthcare.tfaservice.service.interfaces.ITfaService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.math.BigInteger.ONE;

@Service
@ConditionalOnProperty(name = "service.mode", havingValue = "dummy", matchIfMissing = false)
public class DummyTfaService implements ITfaService {
    @Override
    public TfaResponse generateOtp(TfaRequest tfaRequest) {
        return new TfaResponse(String.valueOf(ONE).repeat(4), UUID.randomUUID().toString(), 10);
    }

    @Override
    public Boolean validateOtp(TfaVerifyRequest tfaVerifyRequest) {
        return true;
    }
}

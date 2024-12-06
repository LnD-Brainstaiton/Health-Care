package com.healthcare.tfaservice.service.impl;

import com.healthcare.tfaservice.common.utils.DateTimeUtils;
import com.healthcare.tfaservice.common.utils.OtpUtils;
import com.healthcare.tfaservice.domain.entity.OneTimePassword;
import com.healthcare.tfaservice.domain.request.TfaRequest;
import com.healthcare.tfaservice.domain.response.TfaResponse;
import com.healthcare.tfaservice.repository.OneTimePasswordRepository;
import com.healthcare.tfaservice.service.interfaces.ITwoFactorGeneratorService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;


@Service
public class TwoFactorOtpGeneratorService implements ITwoFactorGeneratorService {

    @Autowired
    private OneTimePasswordRepository oneTimePasswordRepository;

    @Autowired
    private OtpUtils otpUtils;

    private final SecureRandom secureRandom = new SecureRandom();

    private final Random random = new Random();

    private static final Integer UPPER_BOUND = 10;

    @Value("${otp.length}")
    private int otpLength;

    @Value("${otp.otp-validity-in-minute:}")
    private int otpValidityInMinute;

    @Override
    @Transactional
    public TfaResponse getOtp(TfaRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException {
        TfaResponse tfaResponse = new TfaResponse();
        tfaResponse.setSessionId(UUID.randomUUID().toString());
        tfaResponse.setGeneratedOtp(generateOtp(otpLength));
        tfaResponse.setOtpValidityInMinute(otpValidityInMinute);

        updateAndSaveToDatabase(request.getUserName(), tfaResponse);

        return tfaResponse;
    }


    private String generateOtp(int otpLength) {
        IntUnaryOperator intUnary = i -> secureRandom.nextInt(UPPER_BOUND);
        return IntStream
                .iterate(random.nextInt(UPPER_BOUND), intUnary)
                .limit(otpLength)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    private void updateAndSaveToDatabase(String userName, TfaResponse tfaResponse) throws NoSuchAlgorithmException, InvalidKeySpecException {
        OneTimePassword oneTimePasswordForUser = oneTimePasswordRepository.findByUserName(userName);

        if (ObjectUtils.isEmpty(oneTimePasswordForUser)) {
            oneTimePasswordForUser = new OneTimePassword();
        }

        LocalDateTime currentTime = LocalDateTime.now();

        oneTimePasswordForUser.setUserName(userName);
        oneTimePasswordForUser.setGeneratedOtp(otpUtils.generateHash(tfaResponse.getGeneratedOtp()));
        oneTimePasswordForUser.setSessionId(tfaResponse.getSessionId());
        oneTimePasswordForUser.setOtpExpireTime(DateTimeUtils.addMinutes(currentTime, otpValidityInMinute));
        oneTimePasswordForUser.setCreatedBy(userName);
        oneTimePasswordForUser.setCreatedAt(currentTime);

        oneTimePasswordRepository.save(oneTimePasswordForUser);
    }

}

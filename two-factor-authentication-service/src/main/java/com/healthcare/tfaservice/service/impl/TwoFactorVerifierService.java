package com.healthcare.tfaservice.service.impl;

import com.healthcare.tfaservice.common.exceptions.InvalidRequestDataException;
import com.healthcare.tfaservice.common.exceptions.RecordNotFoundException;
import com.healthcare.tfaservice.common.utils.OtpUtils;
import com.healthcare.tfaservice.domain.entity.OneTimePassword;
import com.healthcare.tfaservice.domain.entity.UserBadOtpCount;
import com.healthcare.tfaservice.domain.enums.ResponseMessage;
import com.healthcare.tfaservice.domain.request.TfaVerifyRequest;
import com.healthcare.tfaservice.repository.OneTimePasswordRepository;
import com.healthcare.tfaservice.repository.UserBadOtpCountRepository;
import com.healthcare.tfaservice.service.interfaces.ITwoFactorVerifierService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;


@Service
public class TwoFactorVerifierService implements ITwoFactorVerifierService {

    @Autowired
    OneTimePasswordRepository oneTimePasswordRepository;

    @Autowired
    UserBadOtpCountRepository userBadOtpCountRepository;

    @Autowired
    private OtpUtils otpUtils;

    @Override
    public Boolean verifyOtp(TfaVerifyRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException {
        OneTimePassword otpForUser = oneTimePasswordRepository.findBySessionIdAndUserName(request.getSessionId(), request.getUserName());

        if (ObjectUtils.isEmpty(otpForUser)) {
            throw new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND);
        }

        LocalDateTime currentTime = LocalDateTime.now();

        if (currentTime.isAfter(otpForUser.getOtpExpireTime())) {
            throw new InvalidRequestDataException(ResponseMessage.OTP_EXPIRED);
        }

        boolean validOtp = otpUtils.validateOtp(request.getGeneratedOtp(), otpForUser.getGeneratedOtp());

        if (!validOtp) {
            updateBadOtpCounter(request.getUserName());
        }

        return validOtp;
    }

    private void updateBadOtpCounter(String userName) {
        UserBadOtpCount userBadOtpCount = userBadOtpCountRepository.findConfigurationByUser(userName);

        if (ObjectUtils.isEmpty(userBadOtpCount))
            throw new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND);

        if (ObjectUtils.isEmpty(userBadOtpCount.getConsecutiveBadAttempts())
                || userBadOtpCount.getConsecutiveBadAttempts() == 0) {
            userBadOtpCount.setConsecutiveBadAttempts(1);
        } else {
            userBadOtpCount.setConsecutiveBadAttempts(userBadOtpCount.getConsecutiveBadAttempts() + 1);
        }

        userBadOtpCount.setUpdatedBy(userName);
        userBadOtpCount.setUpdatedAt(LocalDateTime.now());

        userBadOtpCountRepository.save(userBadOtpCount);
    }
}

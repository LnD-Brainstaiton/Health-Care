package com.healthcare.tfaservice.service.impl;

import com.healthcare.tfaservice.common.exceptions.BadOtpAttemptLimitExceedException;
import com.healthcare.tfaservice.common.exceptions.RecordNotFoundException;
import com.healthcare.tfaservice.common.utils.DateTimeUtils;
import com.healthcare.tfaservice.domain.entity.UserBadOtpCount;
import com.healthcare.tfaservice.domain.enums.ResponseMessage;
import com.healthcare.tfaservice.domain.request.TfaRequest;
import com.healthcare.tfaservice.domain.request.TfaVerifyRequest;
import com.healthcare.tfaservice.domain.response.TfaResponse;
import com.healthcare.tfaservice.service.interfaces.ITfaService;
import com.healthcare.tfaservice.service.interfaces.ITwoFactorGeneratorService;
import com.healthcare.tfaservice.service.interfaces.ITwoFactorVerifierService;
import com.healthcare.tfaservice.service.interfaces.IUserTfaBadOtpCountService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@ConditionalOnProperty(name = "service.mode", havingValue = "real", matchIfMissing = false)
public class TfaService implements ITfaService {

    @Autowired
    IUserTfaBadOtpCountService userTfaBadOdtCountService;

    @Autowired
    ITwoFactorGeneratorService twoFactorGeneratorService;

    @Autowired
    ITwoFactorVerifierService twoFactorVerifierService;

    //Todo: temporary kept it in application.yml, need to discuss if it will be in db
    @Value("${otp.bad-odt-limit}")
    private int badOdtLimit;

    @Override
    public TfaResponse generateOtp(TfaRequest tfaRequest) throws NoSuchAlgorithmException, InvalidKeySpecException {
        validateBadOtpCount(tfaRequest.getUserName());

        TfaResponse tfaResponse = twoFactorGeneratorService.getOtp(tfaRequest);

        return tfaResponse;
    }

    @Override
    public Boolean validateOtp(TfaVerifyRequest tfaVerifyRequest) throws NoSuchAlgorithmException, InvalidKeySpecException {
        validateBadOtpCount(tfaVerifyRequest.getUserName());
        return twoFactorVerifierService.verifyOtp(tfaVerifyRequest);
    }

    private void validateBadOtpCount(String userName) {
        UserBadOtpCount userBadOtpCount = userTfaBadOdtCountService.findAndSaveIfNotExist(
                userName);

        if (ObjectUtils.isEmpty(userBadOtpCount)) {
            throw new RecordNotFoundException(ResponseMessage.TFA_CONFIGURATION_NOT_FOUND_EXCEPTION);
        }

        if (isBadAttemptExceeded(userBadOtpCount)) {
            if (ObjectUtils.isEmpty(userBadOtpCount.getTempBlockDate()) || ObjectUtils.isEmpty(userBadOtpCount.getTempUnblockDate())) {
                userTfaBadOdtCountService.setTempBlockAndUnblockDate(userBadOtpCount);
            }

            final LocalDateTime currentTime = LocalDateTime.now();
            if (!isUserEligibleToUnblock(userBadOtpCount.getTempUnblockDate(), currentTime)) {
                final String formattedMsg = DateTimeUtils.getFormattedErrorMessageWithTime(userBadOtpCount.getTempUnblockDate(), currentTime);
                throw new BadOtpAttemptLimitExceedException(formattedMsg);
            }

            userTfaBadOdtCountService.resetUserTfaBadOdtLimit(userBadOtpCount);
        }
    }

    private boolean isBadAttemptExceeded(UserBadOtpCount configuration) {
        int badConsecutiveOtpLimit = badOdtLimit;
        int totalBadAttempts = Objects.isNull(configuration.getConsecutiveBadAttempts()) ? 0 : configuration.getConsecutiveBadAttempts();

        return totalBadAttempts >= badConsecutiveOtpLimit;
    }

    private boolean isUserEligibleToUnblock(final LocalDateTime tempUnblockDate, final LocalDateTime currentTime) {
        return tempUnblockDate.compareTo(currentTime) < 1;
    }

}

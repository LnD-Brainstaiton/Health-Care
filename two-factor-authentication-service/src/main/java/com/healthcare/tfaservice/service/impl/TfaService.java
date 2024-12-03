package com.healthcare.tfaservice.service.impl;

import com.healthcare.tfaservice.common.exceptions.BadOtpAttemptLimitExceedException;
import com.healthcare.tfaservice.common.exceptions.RecordNotFoundException;
import com.healthcare.tfaservice.common.utils.DateTimeUtils;
import com.healthcare.tfaservice.domain.entity.UserTfaBadOdtCount;
import com.healthcare.tfaservice.domain.enums.ResponseMessage;
import com.healthcare.tfaservice.domain.request.TfaRequest;
import com.healthcare.tfaservice.domain.response.TfaResponse;
import com.healthcare.tfaservice.service.interfaces.ITfaService;
import com.healthcare.tfaservice.service.interfaces.ITwoFactorGeneratorService;
import com.healthcare.tfaservice.service.interfaces.IUserTfaBadOdtCountService;
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
    IUserTfaBadOdtCountService userTfaBadOdtCountService;

    @Autowired
    ITwoFactorGeneratorService twoFactorGeneratorService;

    //Todo: temporary kept it in application.yml, need to discuss if it will be in db
    @Value("${otp.bad-odt-limit}")
    private int badOdtLimit;

    @Override
    public TfaResponse generateOtp(TfaRequest tfaRequest) throws NoSuchAlgorithmException, InvalidKeySpecException {
        UserTfaBadOdtCount userTfaBadOdtCount = userTfaBadOdtCountService.findAndSaveIfNotExist(
                tfaRequest.getUserName());

        if (ObjectUtils.isEmpty(userTfaBadOdtCount)) {
            throw new RecordNotFoundException(ResponseMessage.TFA_CONFIGURATION_NOT_FOUND_EXCEPTION);
        }

        if (isBadAttemptExceeded(userTfaBadOdtCount)) {
            if (ObjectUtils.isEmpty(userTfaBadOdtCount.getTempBlockDate()) || ObjectUtils.isEmpty(userTfaBadOdtCount.getTempUnblockDate())) {
                userTfaBadOdtCountService.setTempBlockAndUnblockDate(userTfaBadOdtCount);
            }

            final LocalDateTime currentTime = LocalDateTime.now();
            if (!isUserEligibleToUnblock(userTfaBadOdtCount.getTempUnblockDate(), currentTime)) {
                final String formattedMsg = DateTimeUtils.getFormattedErrorMessageWithTime(userTfaBadOdtCount.getTempUnblockDate(), currentTime);
                throw new BadOtpAttemptLimitExceedException(formattedMsg);
            }

            userTfaBadOdtCountService.resetUserTfaBadOdtLimit(userTfaBadOdtCount);
        }

        TfaResponse tfaResponse = twoFactorGeneratorService.getOtp(tfaRequest);

        return tfaResponse;
    }

    private boolean isBadAttemptExceeded(UserTfaBadOdtCount configuration) {
        int badConsecutiveOtpLimit = badOdtLimit;
        int totalBadAttempts = Objects.isNull(configuration.getConsecutiveBadAttempts()) ? 0 : configuration.getConsecutiveBadAttempts();

        return totalBadAttempts >= badConsecutiveOtpLimit;
    }

    private boolean isUserEligibleToUnblock(final LocalDateTime tempUnblockDate, final LocalDateTime currentTime) {
        return tempUnblockDate.compareTo(currentTime) < 1;
    }

}

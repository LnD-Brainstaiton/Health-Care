package com.healthcare.tfaservice.service.impl;

import com.healthcare.tfaservice.common.utils.DateTimeUtils;
import com.healthcare.tfaservice.domain.entity.UserTfaBadOdtCount;
import com.healthcare.tfaservice.repository.UserTfaBadOdtCountRepository;
import com.healthcare.tfaservice.service.interfaces.IUserTfaBadOdtCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class UserBadOdtCountService implements IUserTfaBadOdtCountService {

    @Autowired
    UserTfaBadOdtCountRepository userTfaBadOdtCountRepository;

    @Value("${otp.bad-odt-limit}")
    private int badOdtLimit;

    @Value("${otp.temp-block-in-minute}")
    private int tempBlockInMinute;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UserTfaBadOdtCount findAndSaveIfNotExist(String userName) {
        UserTfaBadOdtCount configurationByUser = userTfaBadOdtCountRepository.findConfigurationByUser(userName);

        if (Objects.nonNull(configurationByUser))
            return configurationByUser;
        else
            return saveNewUserBadOdtCount(userName);
    }

    @Override
    public void setTempBlockAndUnblockDate(UserTfaBadOdtCount userTfaBadOdtCount) {
        blockUserTfaConfigIfBadLimitExceeded(userTfaBadOdtCount);
        userTfaBadOdtCountRepository.save(userTfaBadOdtCount);
    }

    private UserTfaBadOdtCount saveNewUserBadOdtCount(String userName) {
        UserTfaBadOdtCount newUserTfaBadOdtCount = new UserTfaBadOdtCount();

        newUserTfaBadOdtCount.setUserName(userName);
        newUserTfaBadOdtCount.setConsecutiveBadAttempts(0);
        newUserTfaBadOdtCount.setCreatedBy(userName);
        newUserTfaBadOdtCount.setCreatedAt(LocalDateTime.now());

        userTfaBadOdtCountRepository.save(newUserTfaBadOdtCount);

        return newUserTfaBadOdtCount;
    }

    private void blockUserTfaConfigIfBadLimitExceeded(final UserTfaBadOdtCount userTfaBadOdtCount) {
        if (isBadOtpAttemptExceeded(userTfaBadOdtCount.getConsecutiveBadAttempts())) {
            LocalDateTime currentDate = LocalDateTime.now();
            userTfaBadOdtCount.setTempBlockDate(currentDate);
            userTfaBadOdtCount.setTempUnblockDate(DateTimeUtils.addMinutes(currentDate, tempBlockInMinute));
        }
    }

    private boolean isBadOtpAttemptExceeded(final Integer consecutiveBadAttempts) {
        int badConsecutiveOtpLimit = badOdtLimit;
        int totalBadAttempts = Objects.isNull(consecutiveBadAttempts) ? 0 : consecutiveBadAttempts;

        return totalBadAttempts >= badConsecutiveOtpLimit;
    }

    @Override
    public void resetUserTfaBadOdtLimit(final UserTfaBadOdtCount userTfaBadOdtCount) {
        userTfaBadOdtCount.setConsecutiveBadAttempts(0);
        userTfaBadOdtCount.setTempBlockDate(null);
        userTfaBadOdtCount.setTempUnblockDate(null);
        userTfaBadOdtCountRepository.save(userTfaBadOdtCount);
    }

}

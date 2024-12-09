package com.healthcare.tfaservice.service.impl;

import com.healthcare.tfaservice.common.utils.DateTimeUtils;
import com.healthcare.tfaservice.domain.entity.UserBadOtpCount;
import com.healthcare.tfaservice.repository.UserBadOtpCountRepository;
import com.healthcare.tfaservice.service.interfaces.IUserTfaBadOtpCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class UserBadOtpCountService implements IUserTfaBadOtpCountService {

    @Autowired
    UserBadOtpCountRepository userBadOtpCountRepository;

    @Value("${otp.bad-odt-limit}")
    private int badOdtLimit;

    @Value("${otp.temp-block-in-minute}")
    private int tempBlockInMinute;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UserBadOtpCount findAndSaveIfNotExist(String userName) {
        UserBadOtpCount configurationByUser = userBadOtpCountRepository.findConfigurationByUser(userName);

        if (Objects.nonNull(configurationByUser))
            return configurationByUser;
        else
            return saveNewUserBadOdtCount(userName);
    }

    @Override
    public void setTempBlockAndUnblockDate(UserBadOtpCount userBadOtpCount) {
        blockUserTfaConfigIfBadLimitExceeded(userBadOtpCount);
        userBadOtpCountRepository.save(userBadOtpCount);
    }

    private UserBadOtpCount saveNewUserBadOdtCount(String userName) {
        UserBadOtpCount newUserBadOtpCount = new UserBadOtpCount();

        newUserBadOtpCount.setUserName(userName);
        newUserBadOtpCount.setConsecutiveBadAttempts(0);
        newUserBadOtpCount.setCreatedBy(userName);
        newUserBadOtpCount.setCreatedAt(LocalDateTime.now());

        userBadOtpCountRepository.save(newUserBadOtpCount);

        return newUserBadOtpCount;
    }

    private void blockUserTfaConfigIfBadLimitExceeded(final UserBadOtpCount userBadOtpCount) {
        if (isBadOtpAttemptExceeded(userBadOtpCount.getConsecutiveBadAttempts())) {
            LocalDateTime currentDate = LocalDateTime.now();
            userBadOtpCount.setTempBlockDate(currentDate);
            userBadOtpCount.setTempUnblockDate(DateTimeUtils.addMinutes(currentDate, tempBlockInMinute));
        }
    }

    private boolean isBadOtpAttemptExceeded(final Integer consecutiveBadAttempts) {
        int badConsecutiveOtpLimit = badOdtLimit;
        int totalBadAttempts = Objects.isNull(consecutiveBadAttempts) ? 0 : consecutiveBadAttempts;

        return totalBadAttempts >= badConsecutiveOtpLimit;
    }

    @Override
    public void resetUserTfaBadOdtLimit(final UserBadOtpCount userBadOtpCount) {
        userBadOtpCount.setConsecutiveBadAttempts(0);
        userBadOtpCount.setTempBlockDate(null);
        userBadOtpCount.setTempUnblockDate(null);
        userBadOtpCountRepository.save(userBadOtpCount);
    }

}

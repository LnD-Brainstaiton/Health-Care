package com.healthcare.tfaservice.service.interfaces;

import com.healthcare.tfaservice.domain.entity.UserBadOtpCount;

public interface IUserTfaBadOtpCountService {
    UserBadOtpCount findAndSaveIfNotExist(String userName);

    void setTempBlockAndUnblockDate(UserBadOtpCount userBadOtpCount);

    void resetUserTfaBadOdtLimit(UserBadOtpCount userBadOtpCount);
}


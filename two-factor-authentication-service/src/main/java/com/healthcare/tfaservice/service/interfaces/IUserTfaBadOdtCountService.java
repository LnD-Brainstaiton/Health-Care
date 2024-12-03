package com.healthcare.tfaservice.service.interfaces;

import com.healthcare.tfaservice.domain.entity.UserTfaBadOdtCount;

public interface IUserTfaBadOdtCountService {
    UserTfaBadOdtCount findAndSaveIfNotExist(String userName);

    void setTempBlockAndUnblockDate(UserTfaBadOdtCount userTfaBadOdtCount);

    void resetUserTfaBadOdtLimit(UserTfaBadOdtCount userTfaBadOdtCount);
}


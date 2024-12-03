package com.healthcare.tfaservice.repository;

import com.healthcare.tfaservice.domain.entity.ApplicationSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationSettingsRepository extends JpaRepository<ApplicationSetting, Long> {

    ApplicationSetting findBySettingCode(String settingCode);
}

package com.healthcare.userservice.repository;

import com.healthcare.userservice.domain.entity.DoctorSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorSettingRepository extends JpaRepository<DoctorSetting, Long> {
    Optional<DoctorSetting> findByDoctorId(String userIdentity);

    @Query("SELECT d FROM DoctorSetting d WHERE d.unavailabilitySchedule IS NOT NULL")
    List<DoctorSetting> findDoctorsWithUnavailabilitySchedule();
}

package com.health_care.user_service.repository;

import com.health_care.user_service.domain.entity.DoctorTimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorTimeSlotRepository extends JpaRepository<DoctorTimeSlot, Long> {

    Optional<DoctorTimeSlot> findByDoctorIdAndDaysOfWeekContainingIgnoreCase(String doctorId, String daysOfWeek);
}

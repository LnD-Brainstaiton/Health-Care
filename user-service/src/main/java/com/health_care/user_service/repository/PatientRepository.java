package com.health_care.user_service.repository;

import com.health_care.user_service.domain.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> getPatientByMobileAndIsActive(String mobile, Boolean aTrue);
}
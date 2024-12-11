package com.health_care.user_service.repository;

import com.health_care.user_service.domain.entity.Doctor;
import com.health_care.user_service.domain.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> getPatientByMobileAndIsActive(String mobile, Boolean aTrue);

    Optional<Patient> getPatientByPatientIdAndIsActive(String patientId, Boolean aTrue);

    List<Patient> findAllByIsActiveTrue();

    Page<Patient> findAllByIsActiveTrue(Pageable pageable);
}

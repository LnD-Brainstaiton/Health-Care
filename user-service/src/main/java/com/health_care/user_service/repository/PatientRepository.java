package com.health_care.user_service.repository;

import com.health_care.user_service.domain.entity.Doctor;
import com.health_care.user_service.domain.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long>, JpaSpecificationExecutor<Patient> {
    Optional<Patient> getPatientByMobileAndIsActive(String mobile, Boolean aTrue);

    Optional<Patient> getPatientByPatientIdAndIsActive(String patientId, Boolean aTrue);

    List<Patient> findAllByIsActiveTrue();

    List<Patient> findAllByIsActiveTrue(Sort sort);

    Patient findByPatientIdAndIsActiveTrue(String id);

    Optional<Patient> findByMobileAndIsActiveTrue(String mobile);
}

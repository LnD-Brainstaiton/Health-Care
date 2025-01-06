package com.healthcare.userservice.repository;

import com.healthcare.userservice.domain.entity.Patient;
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

    boolean existsByEmail(String email);

    boolean existsByMobile(String mobile);
}

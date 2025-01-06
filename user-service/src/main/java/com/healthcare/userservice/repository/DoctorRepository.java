package com.healthcare.userservice.repository;

import com.healthcare.userservice.domain.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long>, JpaSpecificationExecutor<Doctor> {

    Optional<Doctor> getDoctorByMobileAndIsActive(String mobile, Boolean aTrue);

    Optional<Doctor> getDoctorByDoctorIdAndIsActive(String doctorId, Boolean aTrue);

    Page<Doctor> findAllByIsActiveTrue(Pageable pageable);

    List<Doctor> findAllByIsActiveTrue();

    List<Doctor> findAllByIsActiveTrue(Sort sort);

    Doctor findByDoctorId(String id);

    Optional<Doctor> findByMobileAndIsActiveTrue(String mobile);

    boolean existsByEmail(String email);

    boolean existsByMobile(String mobile);
}

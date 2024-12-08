package com.health_care.user_service.repository;

import com.health_care.user_service.domain.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor,Long> {

    Optional<Doctor> getDoctorByMobileAndIsActive(String mobile, Boolean aTrue);

    Page<Doctor> findAllByIsActiveTrue(Pageable pageable);

    List<Doctor> findAllByIsActiveTrue();
}

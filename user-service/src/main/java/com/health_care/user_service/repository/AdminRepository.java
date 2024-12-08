package com.health_care.user_service.repository;

import com.health_care.user_service.domain.entity.Admin;
import com.health_care.user_service.domain.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    List<Admin> findAllByIsActiveTrue();

    Page<Admin> findAllByIsActiveTrue(Pageable pageable);

    Optional<Admin> getAdminByMobileAndIsActive(String id, Boolean aTrue);
}

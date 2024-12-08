package com.health_care.user_service.repository;

import com.health_care.user_service.domain.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    List<Admin> findAllByIsActiveTrue();

    Optional<Admin> getAdminByMobileAndIsActive(String id, Boolean aTrue);
}

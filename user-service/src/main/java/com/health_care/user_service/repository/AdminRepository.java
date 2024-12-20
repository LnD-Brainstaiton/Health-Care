package com.health_care.user_service.repository;

import com.health_care.user_service.domain.entity.Admin;
import com.health_care.user_service.domain.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long>, JpaSpecificationExecutor<Admin> {
    List<Admin> findAllByIsActiveTrue();

    List<Admin> findAllByIsActiveTrue(Sort sort);

    Optional<Admin> getAdminByMobileAndIsActive(String mobile, Boolean aTrue);

    Optional<Admin> getAdminByAdminIdAndIsActive(String adminId, Boolean aTrue);

    Admin findByAdminId(String id);
}

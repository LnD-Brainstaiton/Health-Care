package com.healthcare.userservice.repository;

import com.healthcare.userservice.domain.entity.Admin;
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

    Optional<Admin> findByMobileAndIsActiveTrue(String mobile);

    boolean existsByEmail(String email);

    boolean existsByMobile(String mobile);
}

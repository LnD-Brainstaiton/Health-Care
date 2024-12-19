package com.healthcare.userservice.repository;

import com.healthcare.userservice.domain.entity.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    List<Admin> findAllByIsActiveTrue();

    Page<Admin> findAllByIsActiveTrue(Pageable pageable);

    Optional<Admin> getAdminByMobileAndIsActive(String id, Boolean aTrue);

    Optional<Admin> getAdminByAdminIdAndIsActive(String adminId, Boolean aTrue);
}

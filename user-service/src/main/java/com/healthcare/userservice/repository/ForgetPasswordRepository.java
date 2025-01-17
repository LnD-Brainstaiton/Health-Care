package com.healthcare.userservice.repository;


import com.healthcare.userservice.domain.entity.ForgetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForgetPasswordRepository extends JpaRepository<ForgetPassword, Long> {
}

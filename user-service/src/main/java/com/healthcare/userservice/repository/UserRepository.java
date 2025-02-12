package com.healthcare.userservice.repository;

import com.healthcare.userservice.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);

    Optional<User> findByUserIdAndIsActiveTrue(String userName);

    Optional<User> findByMobileNumberAndIsActiveTrue(String mobile);

    Optional<User> findByEmailAndIsActiveTrue(String email);
}


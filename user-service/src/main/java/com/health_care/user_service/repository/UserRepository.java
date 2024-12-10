package com.health_care.user_service.repository;

import com.health_care.user_service.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);

    boolean existsByUserName(String userName);

    Optional<User> findByUserId(String userId);
}


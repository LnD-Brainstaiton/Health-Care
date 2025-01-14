package com.healthcare.userservice.repository;

import com.healthcare.userservice.domain.entity.UserPublicApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicApiRepository extends JpaRepository<UserPublicApi, Long> {
    @Query("SELECT u.url FROM UserPublicApi u WHERE u.isActive = true")
    List<String> findActiveUrls();
}
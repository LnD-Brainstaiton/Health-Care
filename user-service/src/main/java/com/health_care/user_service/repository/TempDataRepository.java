package com.health_care.user_service.repository;

import com.health_care.user_service.domain.entity.TempData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TempDataRepository extends JpaRepository<TempData, Long> {
    Optional<TempData> findTopByRequestIdAndIsActive(String requestId, Boolean aTrue);

    boolean existsByFeatureCodeAndDataAndCheckerResponseAndOperationTypeAndRequestUrl(String featureCode, String data, int code, String operationType, String requestUrl);
}

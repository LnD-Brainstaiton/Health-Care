package com.health_care.user_service.repository;

import com.health_care.user_service.domain.entity.TempData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface TempDataRepository extends JpaRepository<TempData, Long>, JpaSpecificationExecutor<TempData> {
    Optional<TempData> findTopByRequestIdAndIsActive(String requestId, Boolean aTrue);

    List<TempData> findAllByFeatureCodeAndIsActiveTrue(String featureCode);

    boolean existsByFeatureCodeAndDataAndCheckerResponseAndOperationTypeAndRequestUrl(String featureCode, String data, int code, String operationType, String requestUrl);

    Optional<TempData> findByRequestIdAndCheckerResponse(String requestId, int code);
}

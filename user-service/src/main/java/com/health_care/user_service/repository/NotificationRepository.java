package com.health_care.user_service.repository;

import com.health_care.user_service.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification , Long> {

    Optional<Notification> findTopByFeatureCodeAndDataAndCheckerResponseAndOperationTypeAndRequestUrl(String featureCode, String data, int code, String operationType, String createAdminUrl);
}

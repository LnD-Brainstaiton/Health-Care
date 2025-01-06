package com.healthcare.notificationservice.repository;


import com.healthcare.notificationservice.domain.entity.DynamicNotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DynamicNotificationTemplateRepository extends JpaRepository<DynamicNotificationTemplate, String> {

    DynamicNotificationTemplate findByNotificationCode(String notificationCode);

}

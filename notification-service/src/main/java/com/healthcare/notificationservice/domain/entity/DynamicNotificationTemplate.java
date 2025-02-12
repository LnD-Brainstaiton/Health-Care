package com.healthcare.notificationservice.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dynamic_notification_template")
public class DynamicNotificationTemplate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "notification_code", nullable = false, unique = true)
    private String notificationCode;

    @Column(name = "template_subject", nullable = false)
    private String templateSubject;

    @Column(name = "template_name", nullable = false)
    private String templateName;

}

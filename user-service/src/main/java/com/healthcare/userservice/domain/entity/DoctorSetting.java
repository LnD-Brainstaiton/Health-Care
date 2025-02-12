package com.healthcare.userservice.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "doctor_setting")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorSetting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key

    @Column(name = "doctor_id", nullable = false)
    private String doctorId;

    @Column(name = "notification_preference")
    private Boolean notificationPreference;

    @Column(name = "unavailability_schedule", columnDefinition = "JSONB")
    private String unavailabilitySchedule;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
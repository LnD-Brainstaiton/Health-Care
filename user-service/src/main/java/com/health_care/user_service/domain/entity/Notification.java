package com.health_care.user_service.domain.entity;

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
@Table(name = "notification")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "REQUEST_TYPE")
    private String requestType; // ex: doctor, appointment, admin

    @Column(name = "MESSAGE")
    private String message; // Declined due to superadmin1, Duly checked and placed for Authorization By superadmin1

    @Column(name = "REQUEST_ID")
    private String requestId; // doctor ID, AdminId, PatientId, Appointment ID

    @Column(name = "OPERATION_TYPE")
    private String operationType; // Create, Updates

    @Column(name = "CHECKER_ID")
    private String checkerId; // Should be AdminId

    @Column(name = "MAKER_ID")
    private String makerId; // Should be AdminId

    @Column(name = "CHECKER_RESPONSE")
    private Integer checkerResponse; // 1 = Accepted, 2 = Rejected, 3 = Pending

    @Column(name = "IS_ACTIVE")
    private Boolean isActive;
}

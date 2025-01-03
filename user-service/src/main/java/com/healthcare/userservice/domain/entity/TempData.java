package com.healthcare.userservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "tempdata")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TempData extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "REQUEST_TYPE")
    private String featureCode; // ex: doctor, appointment, admin

    @Column(name = "REQUEST_ID")
    private String requestId; // ex: Request Id

    @Column(name = "REQUEST_URL")
    private String requestUrl; // ex: doctor, appointment, admin

    @Column(name = "MESSAGE")
    private String message; // Declined due to super admin1, Duly checked and placed for Authorization By super admin1

    @Column(name = "DATA")
    private String data;

    @Column(name = "OPERATION_TYPE")
    private String operationType; // Create, Updates, Delete

    @Column(name = "CHECKER_ID")
    private String checkerId; // Should be AdminId

    @Column(name = "MAKER_ID")
    private String makerId; // Should be AdminId

    @Column(name = "CHECKER_RESPONSE")
    private Integer checkerResponse; // 1 = Accepted, 2 = Rejected, 3 = Pending

    @Column(name = "IS_ACTIVE")
    private Boolean isActive;
}

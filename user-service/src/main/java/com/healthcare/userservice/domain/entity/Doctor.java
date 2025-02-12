package com.healthcare.userservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "doctor")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Doctor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String firstname;

    @Column(nullable = false, length = 50)
    private String lastname;

    @Column(nullable = false, length = 50)
    private String doctorId;

    @Column(nullable = false, unique = true, length = 15)
    private String mobile;

    @Column(length = 10)
    private String gender; // Add validation at the service level for allowed values

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 100)
    private String designation;

    @Column(length = 100)
    private String department;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(columnDefinition = "TEXT")
    private String specialities; // Store comma-separated values; consider using a converter if necessary

    @Column(precision = 10) // Specify only precision
    private double fee;// Add validation for non-negative values at the service level

    @Column(nullable = false,name = "doctor_auth_level")
    private Integer doctorAuthLevel;

    @Column(nullable = false)
    private Integer registrationNo;

    @Column(nullable = false)
    private String bloodGroup;

    @Column(nullable = false)
    private String dob;

}

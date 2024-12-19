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
@Table(name = "patient")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Patient extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String firstname;

    @Column(nullable = false, length = 50)
    private String lastname;

    @Column(nullable = false, length = 50)
    private String patientId;

    @Column(nullable = false, unique = true, length = 15)
    private String mobile;

    @Column(length = 10)
    private String gender; // Add validation at the service level for allowed values

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "age")
    private Integer age;

    @Column(unique = true, length = 20)
    private String nid;

    @Column(nullable = false)
    private Boolean isActive; // Default value is true

    @Column(name = "blood_group", length = 5)
    private String bloodGroup; // Add validation for allowed values at the service level
}

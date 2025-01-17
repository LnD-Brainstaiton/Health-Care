package com.healthcare.userservice.domain.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name= "FORGET_PASSWORD")
public class ForgetPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "RESET_KEY")
    private String resetKey;

    @Column(name = "EXPIRES_AT",nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "IS_USED", nullable = false)
    private Boolean isUsed;


}

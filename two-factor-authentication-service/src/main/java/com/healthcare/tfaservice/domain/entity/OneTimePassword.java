package com.healthcare.tfaservice.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "GENERATED_ONE_TIME_PASSWORD")
public class OneTimePassword extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "SESSION_ID", length = 50)
    private String sessionId;

    @Column(name = "GENERATED_OTP")
    private String generatedOtp;

    @Column(name = "OTP_EXPIRE_TIME")
    private LocalDateTime otpExpireTime;

}

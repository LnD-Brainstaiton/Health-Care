package com.healthcare.tfaservice.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "USER_BAD_OTP_COUNT")
public class UserBadOtpCount extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "CONSECUTIVE_BAD_ATTEMPTS", columnDefinition = "integer default 0")
    private Integer consecutiveBadAttempts;

    @Column(name = "TEMP_BLOCK_DATE")
    private LocalDateTime tempBlockDate;

    @Column(name = "TEMP_UNBLOCK_DATE")
    private LocalDateTime tempUnblockDate;

}

package com.healthcare.userservice.domain.entity;

import com.healthcare.userservice.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String mobileNumber;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role userType;

    @Column(name = "last_logged_in")
    private LocalDateTime lastLoggedIn;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(nullable = false)
    private Boolean isActive; // Default value is true

}

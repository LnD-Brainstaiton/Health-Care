package com.health_care.user_service.domain.entity;

import com.health_care.user_service.domain.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users") // Renamed to 'users' to avoid reserved keyword conflict
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role usertype;

    @Column(name = "last_logged_in")
    private LocalDateTime lastLoggedIn;
}

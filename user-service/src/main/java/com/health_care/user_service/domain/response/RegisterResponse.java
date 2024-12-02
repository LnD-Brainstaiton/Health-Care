package com.health_care.user_service.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RegisterResponse {

    private String userName;
    private String userType;
    private LocalDateTime lastLoggedIn;

}

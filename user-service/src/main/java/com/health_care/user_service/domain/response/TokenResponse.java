package com.health_care.user_service.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponse {

    private String token;
    private String userType;
    private String userId;
}

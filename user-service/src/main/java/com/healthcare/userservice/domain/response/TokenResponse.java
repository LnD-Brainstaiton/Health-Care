package com.healthcare.userservice.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponse {

    private String token;
    private String refreshToken;
    private String userType;
    private String userId;
    private int doctorAuthLevel;
    private String sessionId;
}

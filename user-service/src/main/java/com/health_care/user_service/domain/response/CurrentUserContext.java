package com.health_care.user_service.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrentUserContext {
    private String userId;
    private String userName;
    private String userType;
}

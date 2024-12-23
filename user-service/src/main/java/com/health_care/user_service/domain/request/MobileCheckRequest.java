package com.health_care.user_service.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MobileCheckRequest {
    private String mobile;
    private String userType;
}

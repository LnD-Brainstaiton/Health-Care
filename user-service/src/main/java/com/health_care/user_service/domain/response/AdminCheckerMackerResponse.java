package com.health_care.user_service.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminCheckerMackerResponse {
    private String featureCode;
    private String requestId;
    private String operationType;
}

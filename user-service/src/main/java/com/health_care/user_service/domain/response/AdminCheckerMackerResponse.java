package com.health_care.user_service.domain.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AdminCheckerMackerResponse {
    private String featureCode;
    private String requestId;
    private String operationType;
}

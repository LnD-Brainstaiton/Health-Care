package com.health_care.user_service.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequestTemp {

    private String featureCode;

    private String operationType;

    private String message;

    private String requestUrl;

    private Object data;

}

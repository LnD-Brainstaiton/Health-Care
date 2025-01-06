package com.healthcare.userservice.domain.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TempDataResponse {
    private String featureCode;

    private String operationType;

    private String message;

    private String requestUrl;

    private Object data;

    private String requestId;

    private String status;
}

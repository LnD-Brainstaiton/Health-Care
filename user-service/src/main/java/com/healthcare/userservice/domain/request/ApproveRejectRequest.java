package com.healthcare.userservice.domain.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApproveRejectRequest implements Serializable {
    private String message;
    private String requestId;
    private String status;
    private String featureCode;
}

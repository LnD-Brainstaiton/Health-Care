package com.healthcare.userservice.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MobileCheckRequest {
    private String mobile;
    private String userType;
    private String userId;
}

package com.healthcare.userservice.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailCheckRequest {
    private String email;
    private String userType;
    private String userId;
}

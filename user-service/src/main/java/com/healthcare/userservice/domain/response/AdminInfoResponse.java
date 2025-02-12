package com.healthcare.userservice.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminInfoResponse {
    private String firstname;
    private String lastname;
    private String mobile;
    private String email;
    private String adminId;
}

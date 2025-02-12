package com.healthcare.userservice.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminInfoUpdateRequest {
    private String firstname;
    private String lastname;
    private String mobile;
    private String email;
    private String password;
    private String adminId;

}

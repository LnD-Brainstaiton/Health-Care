package com.healthcare.userservice.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RegisterResponse {

    private String userName;
    private String userType;

}

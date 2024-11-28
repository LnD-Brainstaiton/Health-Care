package com.health_care.user_service.controller;

import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.dto.Demo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class RegistrationResource {

    public ApiResponse<Demo> register() {
        return new ApiResponse<>();
    }
}

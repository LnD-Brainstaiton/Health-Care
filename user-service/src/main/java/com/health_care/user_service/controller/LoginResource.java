package com.health_care.user_service.controller;

import com.health_care.user_service.common.utils.AppUtils;
import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.entity.User;
import com.health_care.user_service.domain.enums.ApiResponseCode;
import com.health_care.user_service.domain.request.LoginRequest;
import com.health_care.user_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(AppUtils.BASE_URL)
public class LoginResource {
    @Autowired
    private AuthService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/token")
    public ApiResponse<String> getToken(@RequestBody LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            return service.generateToken(loginRequest.getUsername());
        } else {
            return new ApiResponse<>(ApiResponseCode.INVALID_REQUEST_DATA.getResponseCode(), "Invalid Access, Please Provide Valid credential", "");
        }
    }

    @GetMapping("/list")
    public ApiResponse<List<User>> getAllUsers() {
        List<User> users = service.getAllUsers();
        ApiResponse<List<User>> response = new ApiResponse<>();
        response.setResponseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode());
        response.setData(users);
        return response;
    }

}

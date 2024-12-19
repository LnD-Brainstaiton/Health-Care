package com.healthcare.userservice.service;

import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.entity.User;
import com.healthcare.userservice.domain.enums.ApiResponseCode;
import com.healthcare.userservice.domain.response.TokenResponse;
import com.healthcare.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    public ApiResponse<TokenResponse> generateToken(String username) {

        TokenResponse tokenResponse = new TokenResponse();
        Optional<User> getUser = userRepository.findByUserName(username);
        tokenResponse.setToken(jwtService.generateToken(username));
        tokenResponse.setUserType(String.valueOf(getUser.get().getUserType()));
        tokenResponse.setUserId(String.valueOf(getUser.get().getUserId()));
        return new ApiResponse<>(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode(), "Token generated successfully", tokenResponse);
    }

}

package com.health_care.user_service.service;

import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.entity.User;
import com.health_care.user_service.domain.enums.ApiResponseCode;
import com.health_care.user_service.domain.enums.Role;
import com.health_care.user_service.domain.response.TokenResponse;
import com.health_care.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final JwtService jwtService;

    private final UserRepository userRepository;

    public ApiResponse<TokenResponse> generateToken(String username) {

        TokenResponse tokenResponse = new TokenResponse();
        Optional<User> getUser = userRepository.findByUserNameAndIsActiveTrue(username);
        if (getUser.isPresent()) {
            tokenResponse.setToken(jwtService.generateToken(username));
            tokenResponse.setUserType(String.valueOf(getUser.get().getUserType()));
            tokenResponse.setUserId(String.valueOf(getUser.get().getUserId()));
            return new ApiResponse<>(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode(), "Token generated successfully", tokenResponse);
        } else {
            return new ApiResponse<>(ApiResponseCode.NO_ACCOUNT_FOUND.getResponseCode(), "No account found", null);
        }
    }

}

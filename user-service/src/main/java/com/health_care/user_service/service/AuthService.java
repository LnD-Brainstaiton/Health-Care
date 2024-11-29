package com.health_care.user_service.service;

import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.entity.User;
import com.health_care.user_service.domain.enums.ApiResponseCode;
import com.health_care.user_service.domain.enums.Role;
import com.health_care.user_service.domain.response.TokenResponse;
import com.health_care.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
        return new ApiResponse<>(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode(), "Token generated successfully", tokenResponse);
    }

    public List<User> getAllUsers() {
        // Dummy data for demonstration purposes
        List<User> users = new ArrayList<>();

        User user1 = new User();
        user1.setId(1L);
        user1.setUserName("john_doe");
        user1.setPassword("encrypted_password_1");
        user1.setUserType(Role.USER);
        user1.setLastLoggedIn(LocalDateTime.now().minusDays(1));

        User user2 = new User();
        user2.setId(2L);
        user2.setUserName("jane_smith");
        user2.setPassword("encrypted_password_2");
        user2.setUserType(Role.ADMIN);
        user2.setLastLoggedIn(LocalDateTime.now().minusHours(5));

        users.add(user1);
        users.add(user2);

        return users;
    }

}

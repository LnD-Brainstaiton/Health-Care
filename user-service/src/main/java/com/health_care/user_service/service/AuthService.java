package com.health_care.user_service.service;

import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.entity.User;
import com.health_care.user_service.domain.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private JwtService jwtService;

    public ApiResponse<String> generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public List<User> getAllUsers() {
        // Dummy data for demonstration purposes
        List<User> users = new ArrayList<>();

        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("john_doe");
        user1.setPassword("encrypted_password_1");
        user1.setUsertype(Role.USER);
        user1.setLastLoggedIn(LocalDateTime.now().minusDays(1));

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("jane_smith");
        user2.setPassword("encrypted_password_2");
        user2.setUsertype(Role.ADMIN);
        user2.setLastLoggedIn(LocalDateTime.now().minusHours(5));

        users.add(user1);
        users.add(user2);

        return users;
    }

}

package com.health_care.user_service.service.Impl;

import com.health_care.user_service.config.AuthConfig;
import com.health_care.user_service.domain.entity.User;
import com.health_care.user_service.domain.enums.Role;
import com.health_care.user_service.domain.mapper.RegisterMapper;
import com.health_care.user_service.domain.request.RegisterRequest;
import com.health_care.user_service.domain.response.RegisterResponse;
import com.health_care.user_service.repository.UserRepository;
import com.health_care.user_service.service.IRegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService implements IRegistrationService {

    private final UserRepository userRepository;
    private final AuthConfig authConfig;
    private final RegisterMapper registerMapper;

    @Override
    public RegisterResponse registerUser(RegisterRequest user) {
        User registeredUser = User.builder()
                .userName(user.getMobile())
                .password(authConfig.passwordEncoder().encode(user.getPassword()))
                .userType(Role.PATIENT)
                .build();
        registeredUser.setCreatedAt(LocalDateTime.now());
        registeredUser.setCreatedBy(user.getMobile());
        User savedUser = userRepository.save(registeredUser);

        return registerMapper.toRegisterResponse(savedUser);
    }
}

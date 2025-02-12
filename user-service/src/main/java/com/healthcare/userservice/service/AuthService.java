package com.healthcare.userservice.service;

import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.entity.Doctor;
import com.healthcare.userservice.domain.entity.User;
import com.healthcare.userservice.domain.enums.ApiResponseCode;
import com.healthcare.userservice.domain.enums.Role;
import com.healthcare.userservice.domain.response.TokenResponse;
import com.healthcare.userservice.repository.DoctorRepository;
import com.healthcare.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final DoctorRepository doctorRepository;

    public ApiResponse<TokenResponse> generateToken(String userId) {

        TokenResponse tokenResponse = new TokenResponse();
        Optional<User> getUser = userRepository.findByUserIdAndIsActiveTrue(userId);

        if (getUser.isPresent()) {
            tokenResponse.setToken(jwtService.generateToken(userId));
            tokenResponse.setUserType(String.valueOf(getUser.get().getUserType()));
            tokenResponse.setUserId(String.valueOf(getUser.get().getUserId()));

            if(getUser.get().getUserType() == Role.DOCTOR){
                Optional<Doctor> doctor = doctorRepository.getDoctorByDoctorIdAndIsActive(userId,Boolean.TRUE);
                doctor.ifPresent(value -> tokenResponse.setDoctorAuthLevel(value.getDoctorAuthLevel()));
            }
            return new ApiResponse<>(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode(), "Token generated successfully", tokenResponse);
        } else {
            return new ApiResponse<>(ApiResponseCode.NO_ACCOUNT_FOUND.getResponseCode(), "No account found", null);
        }
    }

}

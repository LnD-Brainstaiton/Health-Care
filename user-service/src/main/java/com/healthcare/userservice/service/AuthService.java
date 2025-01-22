package com.healthcare.userservice.service;

import com.healthcare.userservice.common.utils.CryptoUtils;
import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.dto.IdTokenDto;
import com.healthcare.userservice.domain.entity.Doctor;
import com.healthcare.userservice.domain.entity.User;
import com.healthcare.userservice.domain.enums.ApiResponseCode;
import com.healthcare.userservice.domain.enums.Role;
import com.healthcare.userservice.domain.response.TokenResponse;
import com.healthcare.userservice.repository.DoctorRepository;
import com.healthcare.userservice.repository.UserRepository;
import com.healthcare.userservice.service.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService extends BaseService {


    private final JwtService jwtService;

    private final RedisService redisService;

    private final UserRepository userRepository;

    private final DoctorRepository doctorRepository;

    private static final String CORRELATION_ID = "correlationId";


    public ApiResponse<TokenResponse> generateToken(String userId) {

        TokenResponse tokenResponse = new TokenResponse();
        Optional<User> user = userRepository.findByUserIdAndIsActiveTrue(userId);

        if (user.isPresent()) {
            tokenResponse.setToken(jwtService.generateToken(userId));
            tokenResponse.setUserType(String.valueOf(user.get().getUserType()));
            tokenResponse.setUserId(String.valueOf(user.get().getUserId()));
            tokenResponse.setSessionId(getCorrelationIdFromRequest());

            if (user.get().getUserType() == Role.DOCTOR) {
                Optional<Doctor> doctor = doctorRepository.getDoctorByDoctorIdAndIsActive(userId, Boolean.TRUE);
                doctor.ifPresent(value -> tokenResponse.setDoctorAuthLevel(value.getDoctorAuthLevel()));
            }

            saveIdTokenToRedis(user.get(), tokenResponse.getSessionId());

            return new ApiResponse<>(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode(), "Token generated successfully", tokenResponse);
        } else {
            return new ApiResponse<>(ApiResponseCode.NO_ACCOUNT_FOUND.getResponseCode(), "No account found", null);
        }
    }

    private void saveIdTokenToRedis(User user, String sessionId) {
        redisService.deleteAllByPrefix(user.getUserId());

        final IdTokenDto idTokenDto = prepareIdTokenDto(user);
        final String idToken = generateIdToken(idTokenDto);

        final String redisSessionId = user.getUserId() + ":" + sessionId;
        redisService.pushIdTokenToRedis(redisSessionId, idToken);

    }

    private IdTokenDto prepareIdTokenDto(final User user) {
        return IdTokenDto
                .builder()
                .userId(CryptoUtils.encrypt(user.getUserId(), encryptionSecretKey))
                .userType(user.getUserType().toString())
                .mobileNumber(user.getMobileNumber())
                .email(user.getEmail())
                .build();
    }

    private String generateIdToken(final IdTokenDto idTokenDto) {
        Map<String, Object> claims = new HashMap<>();
        Map<String, Object> customerData = objectMapper.convertValue(idTokenDto, Map.class);
        claims.putAll(customerData);
        return JwtService.generateToken(claims, idTokenDto.getUserId(), jwtExpiryTime, jwtIdTokenSecretKey);
    }

    private String getCorrelationIdFromRequest() {
        return getHeaderValue(CORRELATION_ID)
                .orElse(generateCorrelationId());
    }

    public String generateCorrelationId() {
        return UUID.randomUUID().toString().replace("-", "");
    }


}

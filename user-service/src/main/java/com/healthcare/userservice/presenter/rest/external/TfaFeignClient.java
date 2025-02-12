package com.healthcare.userservice.presenter.rest.external;

import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.request.TfaRequest;
import com.healthcare.userservice.domain.request.TfaVerifyRequest;
import com.healthcare.userservice.domain.response.TfaResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@FeignClient(name = "TWO-FACTOR-AUTHENTICATION-SERVICE", path = "/api/v1/tfa")
public interface TfaFeignClient {
    @PostMapping("/generate-otp")
    ApiResponse<TfaResponse> generateOtp(@RequestBody TfaRequest tfaRequest);

    @PostMapping("/validate-otp")
    ApiResponse<Boolean> validateOtp(@Valid @RequestBody TfaVerifyRequest tfaVerifyRequest);

}

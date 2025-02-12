package com.healthcare.userservice.presenter.rest.api;

import com.healthcare.userservice.common.utils.AppUtils;
import com.healthcare.userservice.common.utils.ResponseUtils;
import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.enums.ResponseMessage;
import com.healthcare.userservice.domain.request.OtpVerificationRequest;
import com.healthcare.userservice.domain.request.TfaVerifyRequest;
import com.healthcare.userservice.domain.response.TfaResponse;
import com.healthcare.userservice.service.interfaces.IOtpService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping(AppUtils.BASE_URL)
@AllArgsConstructor
public class OtpResource {

    private final IOtpService otpService;

    @PostMapping("/generate-otp")
    public ApiResponse<?> generateAndSendOtp(@RequestBody @Valid OtpVerificationRequest otpVerificationRequest) {
        TfaResponse tfaResponse = otpService.generateAndSendOtp(otpVerificationRequest);
        return ResponseUtils.createResponseObject(ResponseMessage.OPERATION_SUCCESSFUL, tfaResponse);
    }

    @PostMapping("/validate-otp")
    public ApiResponse<Boolean> validateOtp(@Valid @RequestBody TfaVerifyRequest tfaVerifyRequest) {
        return ResponseUtils.createResponseObject(ResponseMessage.OPERATION_SUCCESSFUL, otpService.verifyOtp(tfaVerifyRequest));
    }

}

package com.health_care.user_service.controller;

import com.health_care.user_service.common.utils.AppUtils;
import com.health_care.user_service.common.utils.ResponseUtils;
import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.enums.ResponseMessage;
import com.health_care.user_service.domain.request.RegisterRequest;
import com.health_care.user_service.domain.response.RegisterResponse;
import com.health_care.user_service.service.IRegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppUtils.BASE_URL)
@AllArgsConstructor
public class DoctorResource {

    private final IRegistrationService registrationService;

    @PostMapping("/doctor/create")
    public ApiResponse<RegisterResponse> register(@RequestBody RegisterRequest doctor) {
        RegisterResponse response = registrationService.registerDoctor(doctor);
        return ResponseUtils.createResponseObject(ResponseMessage.OPERATION_SUCCESSFUL, response);
    }


}

package com.health_care.user_service.controller;

import com.health_care.user_service.common.utils.AppUtils;
import com.health_care.user_service.common.utils.ResponseUtils;
import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.enums.ResponseMessage;
import com.health_care.user_service.domain.request.RegistrationRequestTemp;
import com.health_care.user_service.domain.response.AdminCheckerMackerResponse;
import com.health_care.user_service.domain.response.RegisterResponse;
import com.health_care.user_service.service.IAdminCheckerMacker;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppUtils.BASE_URL)
@AllArgsConstructor
public class AdminCheckerMackerResource {

    private final IAdminCheckerMacker iAdminCheckerMacker;

    @PostMapping("/admin/temp/request")
    public ApiResponse<AdminCheckerMackerResponse> saveTemp(@RequestBody RegistrationRequestTemp temp) throws MissingRequestValueException {
        ApiResponse<AdminCheckerMackerResponse> response = iAdminCheckerMacker.saveTemp(temp);
        return response;
    }
}

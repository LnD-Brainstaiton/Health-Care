package com.health_care.user_service.service;

import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.request.RegistrationRequestTemp;
import com.health_care.user_service.domain.response.AdminCheckerMackerResponse;
import org.springframework.web.bind.MissingRequestValueException;

public interface IAdminCheckerMacker {
    ApiResponse<AdminCheckerMackerResponse> saveTemp(RegistrationRequestTemp temp) throws MissingRequestValueException;
}

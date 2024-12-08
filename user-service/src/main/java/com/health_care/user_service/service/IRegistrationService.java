package com.health_care.user_service.service;

import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.request.RegisterRequest;
import com.health_care.user_service.domain.response.AdminInfoResponse;
import com.health_care.user_service.domain.response.CountResponse;
import com.health_care.user_service.domain.response.RegisterResponse;

public interface IRegistrationService {

    RegisterResponse registerUser(RegisterRequest user);

    RegisterResponse registerDoctor(RegisterRequest doctor);

    RegisterResponse registerAdmin(RegisterRequest admin);

    RegisterResponse getAllAdminList();

    ApiResponse<CountResponse> getAdminsCount();

    ApiResponse<AdminInfoResponse> getAdminByMobile(String id);
}

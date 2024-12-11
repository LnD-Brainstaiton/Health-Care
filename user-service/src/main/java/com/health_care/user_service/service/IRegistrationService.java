package com.health_care.user_service.service;

import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.request.AdminInfoUpdateRequest;
import com.health_care.user_service.domain.request.RegisterRequest;
import com.health_care.user_service.domain.response.AdminInfoResponse;
import com.health_care.user_service.domain.response.CountResponse;
import com.health_care.user_service.domain.response.DoctorInfoResponse;
import com.health_care.user_service.domain.response.RegisterResponse;

import java.util.List;

public interface IRegistrationService {

    RegisterResponse registerUser(RegisterRequest user);

    RegisterResponse registerDoctor(RegisterRequest doctor);

    RegisterResponse registerAdmin(RegisterRequest admin);

    ApiResponse<List<AdminInfoResponse>> getAllAdminList(int page, int size, String sort);

    ApiResponse<CountResponse> getAdminsCount();

    ApiResponse<AdminInfoResponse> getAdminByUniqueId(String id);

    ApiResponse<Void> updateAdmin(AdminInfoUpdateRequest request);
}

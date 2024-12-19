package com.healthcare.userservice.service;

import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.request.AdminInfoUpdateRequest;
import com.healthcare.userservice.domain.request.RegisterRequest;
import com.healthcare.userservice.domain.response.AdminInfoResponse;
import com.healthcare.userservice.domain.response.CountResponse;
import com.healthcare.userservice.domain.response.RegisterResponse;

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

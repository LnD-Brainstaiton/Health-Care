package com.healthcare.userservice.service;

import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.request.AdminInfoUpdateRequest;
import com.healthcare.userservice.domain.request.DoctorProfessionalInfoRequest;
import com.healthcare.userservice.domain.request.RegisterRequest;
import com.healthcare.userservice.domain.response.AdminInfoResponse;
import com.healthcare.userservice.domain.response.CountResponse;
import com.healthcare.userservice.domain.response.PaginationResponse;
import com.healthcare.userservice.domain.response.RegisterResponse;

public interface IRegistrationService {

    RegisterResponse registerUser(RegisterRequest user);

    RegisterResponse registerDoctorLeve1(RegisterRequest doctor);

    RegisterResponse registerAdmin(RegisterRequest admin);

    PaginationResponse<AdminInfoResponse> getAllAdminList(int page, int size, String sort, String firstnameLastname, String id);

    ApiResponse<CountResponse> getAdminsCount();

    ApiResponse<AdminInfoResponse> getAdminByUniqueId(String id);

    ApiResponse<Void> updateAdmin(AdminInfoUpdateRequest request);

    ApiResponse<String> deleteDAdminById(String id);

    RegisterResponse registerDoctor(DoctorProfessionalInfoRequest doctor);
}

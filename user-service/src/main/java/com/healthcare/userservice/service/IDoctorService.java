package com.healthcare.userservice.service;

import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.request.DoctorInfoUpdateRequest;
import com.healthcare.userservice.domain.response.CountResponse;
import com.healthcare.userservice.domain.response.DoctorInfoResponse;
import com.healthcare.userservice.domain.response.PaginationResponse;

public interface IDoctorService {
    ApiResponse<Void> updateDoctor(DoctorInfoUpdateRequest request);

    ApiResponse<DoctorInfoResponse> getDoctorById(String id);

    PaginationResponse<DoctorInfoResponse> getAllDoctorInfo(int page, int size, String sort);

    ApiResponse<CountResponse> getDoctorsCount();
}

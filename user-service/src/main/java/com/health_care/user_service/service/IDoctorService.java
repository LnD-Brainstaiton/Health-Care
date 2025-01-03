package com.health_care.user_service.service;

import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.entity.Doctor;
import com.health_care.user_service.domain.request.DoctorInfoUpdateRequest;
import com.health_care.user_service.domain.response.CountResponse;
import com.health_care.user_service.domain.response.DoctorInfoResponse;
import com.health_care.user_service.domain.response.PaginationResponse;

import java.util.List;

public interface IDoctorService {
    ApiResponse<Void> updateDoctor(DoctorInfoUpdateRequest request);

    ApiResponse<DoctorInfoResponse> getDoctorById(String id);

    PaginationResponse<DoctorInfoResponse> getAllDoctorInfo(int page, int size, String sort, String firstnameLastname, String id,
                                                            String department, String designation, String gender);

    ApiResponse<CountResponse> getDoctorsCount();

    ApiResponse<String> deleteDoctorById(String id);
}

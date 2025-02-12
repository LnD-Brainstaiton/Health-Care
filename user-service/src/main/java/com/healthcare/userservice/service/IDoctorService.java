package com.healthcare.userservice.service;

import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.request.BmdcValidationRequest;
import com.healthcare.userservice.domain.request.DoctorInfoUpdateRequest;
import com.healthcare.userservice.domain.request.DoctorVacationRequest;
import com.healthcare.userservice.domain.request.TimeSlotRequest;
import com.healthcare.userservice.domain.response.CountResponse;
import com.healthcare.userservice.domain.response.DoctorInfoResponse;
import com.healthcare.userservice.domain.response.PaginationResponse;
import com.healthcare.userservice.domain.response.TimeSlotResponse;

public interface IDoctorService {
    ApiResponse<Void> updateDoctor(DoctorInfoUpdateRequest request);

    ApiResponse<DoctorInfoResponse> getDoctorById(String id);

    PaginationResponse<DoctorInfoResponse> getAllDoctorInfo(int page, int size, String sort, String firstnameLastname, String id,
                                                            String department, String designation, String gender);

    ApiResponse<CountResponse> getDoctorsCount();

    ApiResponse<String> deleteDoctorById(String id);

    TimeSlotResponse getTimeSlotList(TimeSlotRequest request);

    ApiResponse<String> getCaptcha();

    ApiResponse<String> validateRegistration(BmdcValidationRequest request);

    ApiResponse<Void> vacationRequest(DoctorVacationRequest request);
}

package com.healthcare.userservice.service;

import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.request.PatientInfoUpdateRequest;
import com.healthcare.userservice.domain.response.CountResponse;
import com.healthcare.userservice.domain.response.PaginationResponse;
import com.healthcare.userservice.domain.response.PatientInfoResponse;

public interface IPatientService {
    ApiResponse<Void> updatePatient(PatientInfoUpdateRequest request);

    ApiResponse<PatientInfoResponse> getPatientById(String id);

    ApiResponse<CountResponse> getAPatientsCount();

    PaginationResponse<PatientInfoResponse> getAllPatientList(int page, int size, String sort, String firstnameLastname, String id);

    ApiResponse<String> deletePatient(String id);
}

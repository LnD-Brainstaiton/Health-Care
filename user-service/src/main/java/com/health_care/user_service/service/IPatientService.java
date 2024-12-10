package com.health_care.user_service.service;

import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.entity.Patient;
import com.health_care.user_service.domain.request.PatientInfoUpdateRequest;
import com.health_care.user_service.domain.response.CountResponse;
import com.health_care.user_service.domain.response.PatientInfoResponse;

import java.util.List;

public interface IPatientService {
    ApiResponse<Void> updatePatient(PatientInfoUpdateRequest request);

    ApiResponse<Patient> getPatientById(String id);

    ApiResponse<CountResponse> getAPatientsCount();

    ApiResponse<List<PatientInfoResponse>> getAllPatientList(int page, int size, String sort);
}

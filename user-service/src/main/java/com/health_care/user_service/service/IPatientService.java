package com.health_care.user_service.service;

import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.entity.Patient;
import com.health_care.user_service.domain.request.PatientInfoUpdateRequest;
import com.health_care.user_service.domain.response.CountResponse;

public interface IPatientService {
    ApiResponse<Void> updatePatient(PatientInfoUpdateRequest request);

    ApiResponse<Patient> getPatientById(String id);

    ApiResponse<CountResponse> getAPatientsCount();
}

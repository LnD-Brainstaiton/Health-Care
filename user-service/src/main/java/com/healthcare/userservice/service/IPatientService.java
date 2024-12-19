package com.healthcare.userservice.service;

import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.entity.Patient;
import com.healthcare.userservice.domain.request.PatientInfoUpdateRequest;
import com.healthcare.userservice.domain.response.CountResponse;
import com.healthcare.userservice.domain.response.PatientInfoResponse;

import java.util.List;

public interface IPatientService {
    ApiResponse<Void> updatePatient(PatientInfoUpdateRequest request);

    ApiResponse<Patient> getPatientById(String id);

    ApiResponse<CountResponse> getAPatientsCount();

    ApiResponse<List<PatientInfoResponse>> getAllPatientList(int page, int size, String sort);
}

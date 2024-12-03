package com.health_care.user_service.service.Impl;

import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.entity.Patient;
import com.health_care.user_service.domain.enums.ApiResponseCode;
import com.health_care.user_service.domain.enums.ResponseMessage;
import com.health_care.user_service.domain.request.PatientInfoUpdateRequest;
import com.health_care.user_service.repository.PatientRepository;
import com.health_care.user_service.service.IPatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements IPatientService {

    private final PatientRepository patientRepository;

    @Override
    public ApiResponse<Void> updatePatient(PatientInfoUpdateRequest request) {
        return patientRepository.getPatientByMobileAndIsActive(request.getMobile(), Boolean.TRUE)
                .map(patient -> updatePatientDetails(patient, request))
                .orElseGet(() -> ApiResponse.<Void>builder()
                        .responseCode(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode())
                        .responseMessage(ResponseMessage.RECORD_NOT_FOUND.getResponseMessage())
                        .build()
                );
    }

    @Override
    public ApiResponse<Patient> getPatientById(String id) {
        return patientRepository.getPatientByMobileAndIsActive(id, Boolean.TRUE)
                .map(patient -> ApiResponse.<Patient>builder()
                        .data(patient)
                        .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                        .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                        .build()
                )
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        ResponseMessage.RECORD_NOT_FOUND.getResponseMessage()
                ));
    }

    private ApiResponse<Void> updatePatientDetails(Patient patient, PatientInfoUpdateRequest request) {
        patient.setAge(request.getAge());
        patient.setNid(request.getNid());
        patient.setAddress(request.getAddress());
        patient.setGender(request.getGender());
        patient.setBloodGroup(request.getBloodGroup());
        patientRepository.save(patient);

        return ApiResponse.<Void>builder()
                .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                .build();
    }
}

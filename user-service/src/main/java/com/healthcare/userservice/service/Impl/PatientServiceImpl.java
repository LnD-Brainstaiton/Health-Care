package com.healthcare.userservice.service.Impl;

import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.entity.Patient;
import com.healthcare.userservice.domain.enums.ApiResponseCode;
import com.healthcare.userservice.domain.enums.ResponseMessage;
import com.healthcare.userservice.domain.mapper.PatientMapper;
import com.healthcare.userservice.domain.request.PatientInfoUpdateRequest;
import com.healthcare.userservice.domain.response.CountResponse;
import com.healthcare.userservice.domain.response.PatientInfoResponse;
import com.healthcare.userservice.repository.PatientRepository;
import com.healthcare.userservice.service.IPatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements IPatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

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
        return patientRepository.getPatientByPatientIdAndIsActive(id, Boolean.TRUE)
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

    @Override
    public ApiResponse<CountResponse> getAPatientsCount() {
        List<Patient> patients = patientRepository.findAllByIsActiveTrue();
        CountResponse countResponse = new CountResponse();
        countResponse.setCount(patients.size());
        return ApiResponse.<CountResponse>builder()
                .data(countResponse)
                .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                .build();
    }



    @Override
    public ApiResponse<List<PatientInfoResponse>> getAllPatientList(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc(sort)));
        Page<Patient> activePatientsPage = patientRepository.findAllByIsActiveTrue(pageable);

        if (activePatientsPage.isEmpty()) {
            return ApiResponse.<List<PatientInfoResponse>>builder()
                    .data(Collections.emptyList())
                    .responseCode(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode())
                    .responseMessage(ResponseMessage.RECORD_NOT_FOUND.getResponseMessage())
                    .build();
        }

        List<PatientInfoResponse> patientInfoResponses = activePatientsPage.getContent().stream()
                .map(patientMapper::toPatientInfoResponse)
                .collect(Collectors.toList());

        return ApiResponse.<List<PatientInfoResponse>>builder()
                .data(patientInfoResponses)
                .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                .build();
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

package com.healthcare.userservice.service.Impl;

import com.healthcare.userservice.config.AuthConfig;
import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.entity.Patient;
import com.healthcare.userservice.domain.entity.User;
import com.healthcare.userservice.domain.enums.ApiResponseCode;
import com.healthcare.userservice.domain.enums.ResponseMessage;
import com.healthcare.userservice.domain.mapper.PatientMapper;
import com.healthcare.userservice.domain.request.PatientInfoUpdateRequest;
import com.healthcare.userservice.domain.response.CountResponse;
import com.healthcare.userservice.domain.response.PaginationResponse;
import com.healthcare.userservice.domain.response.PatientInfoResponse;
import com.healthcare.userservice.repository.PatientRepository;
import com.healthcare.userservice.repository.UserRepository;
import com.healthcare.userservice.repository.specification.PatientSpecification;
import com.healthcare.userservice.service.IPatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements IPatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final UserRepository userRepository;
    private final AuthConfig authConfig;

    @Override
    public ApiResponse<Void> updatePatient(PatientInfoUpdateRequest request) {
        return patientRepository.getPatientByPatientIdAndIsActive(request.getPatientId(), Boolean.TRUE)
                .map(patient -> updatePatientDetails(patient, request))
                .orElseGet(() -> ApiResponse.<Void>builder()
                        .responseCode(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode())
                        .responseMessage(ResponseMessage.RECORD_NOT_FOUND.getResponseMessage())
                        .build()
                );
    }

    @Override
    public ApiResponse<PatientInfoResponse> getPatientById(String id) {
        Optional<Patient> patientResponse = patientRepository.getPatientByPatientIdAndIsActive(id, Boolean.TRUE);
        return patientResponse.map(patient -> {
                    PatientInfoResponse response = patientMapper.toPatientInfoResponse(patient);
                    return ApiResponse.<PatientInfoResponse>builder()
                            .data(response)
                            .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                            .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                            .build();
                })
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
    public PaginationResponse<PatientInfoResponse> getAllPatientList(int page, int size, String sort, String firstnameLastname, String id) {
        Sort.Order sortOrder;
        try {
            sortOrder = Sort.Order.asc(sort);
        } catch (IllegalArgumentException ex) {
            return new PaginationResponse<>(
                    Collections.emptyList(),
                    0L
            );
        }
        // Check if page = -1, fetch all active doctors without pagination
        if (page == -1) {
            List<Patient> activePatient = patientRepository.findAllByIsActiveTrue(Sort.by(sortOrder));
            List<PatientInfoResponse> patientInfoResponses = activePatient.stream()
                    .map(patientMapper::toPatientInfoResponse)
                    .collect(Collectors.toList());
            return new PaginationResponse<>(patientInfoResponses, (long) patientInfoResponses.size());
        }

        int validatedPage = Math.max(0, page);
        int validatedSize = Math.max(1, size);

        Pageable pageable = PageRequest.of(validatedPage, validatedSize, Sort.by(sortOrder));
        Specification<Patient> spec = Specification.where(PatientSpecification.hasFirstnameLastname(firstnameLastname))
                .and(PatientSpecification.hasId(id))
                .and(PatientSpecification.isActive());
        Page<Patient> activePatientsPage = patientRepository.findAll(spec, pageable);

        if (activePatientsPage.isEmpty()) {
            return new PaginationResponse<>(
                    Collections.emptyList(),
                    0L
            );
        }

        List<PatientInfoResponse> patientInfoResponses = activePatientsPage.getContent().stream()
                .map(patientMapper::toPatientInfoResponse)
                .collect(Collectors.toList());

        return new PaginationResponse<>(
                activePatientsPage.getNumber(),
                activePatientsPage.getSize(),
                activePatientsPage.getTotalElements(),
                activePatientsPage.getTotalPages(),
                patientInfoResponses
        );
    }

    @Override
    public ApiResponse<String> deletePatient(String id) {
        Patient patient = patientRepository.findByPatientIdAndIsActiveTrue(id);
        patient.setIsActive(false);
        patientRepository.save(patient);
        Optional<User> user = userRepository.findByUserId(id);
        if (user.isPresent()) {
            user.get().setIsActive(false);
            userRepository.save(user.get());
        } else {
            return ApiResponse.<String>builder()
                    .responseMessage(ResponseMessage.RECORD_NOT_FOUND.getResponseMessage())
                    .responseCode(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode())
                    .build();
        }

        return ApiResponse.<String>builder()
                .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                .build();
    }

    private ApiResponse<Void> updatePatientDetails(Patient patient, PatientInfoUpdateRequest request) {
        patient.setFirstname(request.getFirstname());
        patient.setLastname(request.getLastname());
        patient.setEmail(request.getEmail());
        patient.setMobile(request.getMobile());
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

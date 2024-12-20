package com.health_care.user_service.service.Impl;

import com.health_care.user_service.config.AuthConfig;
import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.entity.Admin;
import com.health_care.user_service.domain.entity.Patient;
import com.health_care.user_service.domain.entity.User;
import com.health_care.user_service.domain.enums.ApiResponseCode;
import com.health_care.user_service.domain.enums.ResponseMessage;
import com.health_care.user_service.domain.mapper.PatientMapper;
import com.health_care.user_service.domain.request.PatientInfoUpdateRequest;
import com.health_care.user_service.domain.response.AdminInfoResponse;
import com.health_care.user_service.domain.response.CountResponse;
import com.health_care.user_service.domain.response.PaginationResponse;
import com.health_care.user_service.domain.response.PatientInfoResponse;
import com.health_care.user_service.repository.PatientRepository;
import com.health_care.user_service.repository.UserRepository;
import com.health_care.user_service.repository.specification.AdminSpecification;
import com.health_care.user_service.repository.specification.PatientSpecification;
import com.health_care.user_service.service.IPatientService;
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
        Page<Patient> activePatientsPage = patientRepository.findAll(spec,pageable);

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
        if(user.isPresent()) {
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

        Optional<User> user = userRepository.findByUserId(request.getPatientId());
        if(user.isPresent()) {
            if(!Objects.equals(request.getPassword(), "")) {
                user.get().setPassword(authConfig.passwordEncoder().encode(request.getPassword()));
            }
            user.get().setUserName(request.getMobile());
            userRepository.save(user.get());
        }

        return ApiResponse.<Void>builder()
                .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                .build();
    }
}

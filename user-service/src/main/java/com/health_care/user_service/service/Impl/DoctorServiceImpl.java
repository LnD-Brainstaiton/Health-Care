package com.health_care.user_service.service.Impl;

import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.entity.Doctor;
import com.health_care.user_service.domain.enums.ApiResponseCode;
import com.health_care.user_service.domain.enums.ResponseMessage;
import com.health_care.user_service.domain.mapper.DoctorMapper;
import com.health_care.user_service.domain.request.DoctorInfoUpdateRequest;
import com.health_care.user_service.domain.response.DoctorInfoResponse;
import com.health_care.user_service.repository.DoctorRepository;
import com.health_care.user_service.service.IDoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements IDoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    @Override
    public ApiResponse<Void> updateDoctor(DoctorInfoUpdateRequest request) {
        return doctorRepository.getDoctorByMobileAndIsActive(request.getMobile(), Boolean.TRUE)
                .map(doctor -> updateDoctorDetails(doctor, request))
                .orElseGet(() -> ApiResponse.<Void>builder()
                        .responseCode(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode())
                        .responseMessage(ResponseMessage.RECORD_NOT_FOUND.getResponseMessage())
                        .build());
    }


    @Override
    public ApiResponse<DoctorInfoResponse> getDoctorById(String id) {
        Optional<Doctor> doctorOptional = doctorRepository.getDoctorByMobileAndIsActive(id, Boolean.TRUE);

        return doctorOptional.map(doctor -> {
            DoctorInfoResponse response = doctorMapper.toDoctorInfoResponse(doctor);
            return ApiResponse.<DoctorInfoResponse>builder()
                    .data(response)
                    .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                    .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                    .build();
        }).orElseGet(() -> ApiResponse.<DoctorInfoResponse>builder()
                .responseCode(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode())
                .responseMessage(ResponseMessage.RECORD_NOT_FOUND.getResponseMessage())
                .build());
    }

    @Override
    public ApiResponse<List<DoctorInfoResponse>> getAllDoctorInfo(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc(sort)));
        Page<Doctor> activeDoctorsPage = doctorRepository.findAllByIsActiveTrue(pageable);

        if (activeDoctorsPage.isEmpty()) {
            return ApiResponse.<List<DoctorInfoResponse>>builder()
                    .data(Collections.emptyList())
                    .responseCode(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode())
                    .responseMessage(ResponseMessage.RECORD_NOT_FOUND.getResponseMessage())
                    .build();
        }

        List<DoctorInfoResponse> doctorInfoResponses = activeDoctorsPage.getContent().stream()
                .map(doctorMapper::toDoctorInfoResponse)
                .collect(Collectors.toList());

        return ApiResponse.<List<DoctorInfoResponse>>builder()
                .data(doctorInfoResponses)
                .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                .build();
    }

    private ApiResponse<Void> updateDoctorDetails(Doctor doctor, DoctorInfoUpdateRequest request) {
        doctor.setDepartment(request.getDepartment());
        doctor.setDesignation(request.getDesignation());
        doctor.setFee(request.getFee());
        doctor.setGender(request.getGender());
        doctor.setSpecialities(request.getSpecialities());
        doctorRepository.save(doctor);

        return ApiResponse.<Void>builder()
                .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                .build();
    }
}

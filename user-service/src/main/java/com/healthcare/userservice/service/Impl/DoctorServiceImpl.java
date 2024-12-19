package com.healthcare.userservice.service.Impl;

import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.entity.Doctor;
import com.healthcare.userservice.domain.enums.ApiResponseCode;
import com.healthcare.userservice.domain.enums.ResponseMessage;
import com.healthcare.userservice.domain.mapper.DoctorMapper;
import com.healthcare.userservice.domain.request.DoctorInfoUpdateRequest;
import com.healthcare.userservice.domain.response.CountResponse;
import com.healthcare.userservice.domain.response.DoctorInfoResponse;
import com.healthcare.userservice.domain.response.PaginationResponse;
import com.healthcare.userservice.repository.DoctorRepository;
import com.healthcare.userservice.service.IDoctorService;
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
        Optional<Doctor> doctorOptional = doctorRepository.getDoctorByDoctorIdAndIsActive(id, Boolean.TRUE);

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
    public PaginationResponse<DoctorInfoResponse> getAllDoctorInfo(int page, int size, String sort) {
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
            List<Doctor> activeDoctors = doctorRepository.findAllByIsActiveTrue(Sort.by(sortOrder));
            List<DoctorInfoResponse> doctorInfoResponses = activeDoctors.stream()
                    .map(doctorMapper::toDoctorInfoResponse)
                    .collect(Collectors.toList());
            return new PaginationResponse<>(doctorInfoResponses, (long) doctorInfoResponses.size());
        }

        int validatedPage = Math.max(0, page);
        int validatedSize = Math.max(1, size);

        Pageable pageable = PageRequest.of(validatedPage, validatedSize, Sort.by(sortOrder));
        Page<Doctor> activeDoctorsPage = doctorRepository.findAllByIsActiveTrue(pageable);

        if (activeDoctorsPage.isEmpty()) {
            return new PaginationResponse<>(
                    Collections.emptyList(),
                    0L
            );
        }

        List<DoctorInfoResponse> doctorInfoResponses = activeDoctorsPage.getContent().stream()
                .map(doctorMapper::toDoctorInfoResponse)
                .collect(Collectors.toList());

        return new PaginationResponse<>(
                activeDoctorsPage.getNumber(),
                activeDoctorsPage.getSize(),
                activeDoctorsPage.getTotalElements(),
                activeDoctorsPage.getTotalPages(),
                doctorInfoResponses
        );
    }



    @Override
    public ApiResponse<CountResponse> getDoctorsCount() {
        List<Doctor> doctors = doctorRepository.findAllByIsActiveTrue();
        CountResponse countResponse = new CountResponse();
        countResponse.setCount(doctors.size());
        return ApiResponse.<CountResponse>builder()
                .data(countResponse)
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

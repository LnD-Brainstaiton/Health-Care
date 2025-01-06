package com.healthcare.userservice.service.Impl;

import com.healthcare.userservice.config.AuthConfig;
import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.entity.Doctor;
import com.healthcare.userservice.domain.entity.User;
import com.healthcare.userservice.domain.enums.ApiResponseCode;
import com.healthcare.userservice.domain.enums.ResponseMessage;
import com.healthcare.userservice.domain.mapper.DoctorMapper;
import com.healthcare.userservice.domain.request.DoctorInfoUpdateRequest;
import com.healthcare.userservice.domain.response.CountResponse;
import com.healthcare.userservice.domain.response.DoctorInfoResponse;
import com.healthcare.userservice.domain.response.PaginationResponse;
import com.healthcare.userservice.repository.DoctorRepository;
import com.healthcare.userservice.repository.UserRepository;
import com.healthcare.userservice.repository.specification.DoctorSpecification;
import com.healthcare.userservice.service.IDoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements IDoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final UserRepository userRepository;
    private final AuthConfig authConfig;

    @Override
    public ApiResponse<Void> updateDoctor(DoctorInfoUpdateRequest request) {
        return doctorRepository.getDoctorByDoctorIdAndIsActive(request.getDoctorId(), Boolean.TRUE)
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
    public PaginationResponse<DoctorInfoResponse> getAllDoctorInfo(int page, int size, String sort, String firstnameLastname, String id,
                                                                   String department, String designation, String gender) {
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
        Specification<Doctor> spec = Specification.where(DoctorSpecification.hasDesignation(designation))
                .and(DoctorSpecification.hasDepartment(department))
                .and(DoctorSpecification.hasSpecialities(gender))
                .and(DoctorSpecification.hasFirstnameLastname(firstnameLastname))
                .and(DoctorSpecification.hasId(id))
                .and(DoctorSpecification.isActive());
        Page<Doctor> activeDoctorsPage = doctorRepository.findAll(spec, pageable);

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

    @Override
    public ApiResponse<String> deleteDoctorById(String id) {
        Doctor doctor = doctorRepository.findByDoctorId(id);
        if (doctor == null) {
            return ApiResponse.<String>builder()
                    .responseCode(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode())
                    .responseMessage(ResponseMessage.RECORD_NOT_FOUND.getResponseMessage())
                    .build();
        }
        doctor.setIsActive(false);
        doctorRepository.save(doctor);
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
                .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                .build();
    }

    private ApiResponse<Void> updateDoctorDetails(Doctor doctor, DoctorInfoUpdateRequest request) {
        doctor.setFirstname(request.getFirstname());
        doctor.setLastname(request.getLastname());
        doctor.setMobile(request.getMobile());
        doctor.setDepartment(request.getDepartment());
        doctor.setDesignation(request.getDesignation());
        doctor.setFee(request.getFee());
        doctor.setGender(request.getGender());
        doctor.setSpecialities(request.getSpecialities());
        doctorRepository.save(doctor);

        Optional<User> user = userRepository.findByUserId(request.getDoctorId());
        if (user.isPresent()) {
            if (!Objects.equals(request.getPassword(), "")) {
                user.get().setPassword(authConfig.passwordEncoder().encode(request.getPassword()));
            }
            user.get().setUserName(request.getDoctorId());
            userRepository.save(user.get());
        }

        return ApiResponse.<Void>builder()
                .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                .build();
    }
}

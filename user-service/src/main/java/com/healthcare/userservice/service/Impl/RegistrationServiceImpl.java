package com.healthcare.userservice.service.Impl;


import com.health_care.id.generator.Impl.UniqueIdGeneratorImpl;
import com.healthcare.userservice.common.exceptions.InvalidRequestDataException;
import com.healthcare.userservice.config.AuthConfig;
import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.entity.Admin;
import com.healthcare.userservice.domain.entity.Doctor;
import com.healthcare.userservice.domain.entity.Patient;
import com.healthcare.userservice.domain.entity.User;
import com.healthcare.userservice.domain.enums.ApiResponseCode;
import com.healthcare.userservice.domain.enums.ResponseMessage;
import com.healthcare.userservice.domain.enums.Role;
import com.healthcare.userservice.domain.mapper.AdminMapper;
import com.healthcare.userservice.domain.mapper.RegisterMapper;
import com.healthcare.userservice.domain.request.AdminInfoUpdateRequest;
import com.healthcare.userservice.domain.request.RegisterRequest;
import com.healthcare.userservice.domain.response.AdminInfoResponse;
import com.healthcare.userservice.domain.response.CountResponse;
import com.healthcare.userservice.domain.response.PaginationResponse;
import com.healthcare.userservice.domain.response.RegisterResponse;
import com.healthcare.userservice.repository.AdminRepository;
import com.healthcare.userservice.repository.DoctorRepository;
import com.healthcare.userservice.repository.PatientRepository;
import com.healthcare.userservice.repository.UserRepository;
import com.healthcare.userservice.repository.specification.AdminSpecification;
import com.healthcare.userservice.service.IRegistrationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements IRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationServiceImpl.class);

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AuthConfig authConfig;
    private final AdminMapper adminMapper;
    private final RegisterMapper registerMapper;
    private final UniqueIdGeneratorImpl uniqueIdGenerator;

    @Value("${unique.id.patient.prefix}")
    private String patientPrefix;

    @Value("${unique.id.admin.prefix}")
    private String adminPrefix;

    @Value("${unique.id.doctor.prefix}")
    private String doctorPrefix;

    @Override
    @Transactional
    public RegisterResponse registerUser(RegisterRequest request) {
        return register(request, Role.PATIENT, this::savePatient);
    }

    @Override
    @Transactional
    public RegisterResponse registerDoctor(RegisterRequest request) {
        return register(request, Role.DOCTOR, this::saveDoctor);
    }

    @Override
    @Transactional
    public RegisterResponse registerAdmin(RegisterRequest request) {
        return register(request, Role.ADMIN, this::saveAdmin);
    }

    @Override
    public PaginationResponse<AdminInfoResponse> getAllAdminList(int page, int size, String sort, String firstnameLastname, String id) {
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
            List<Admin> activeAdmins = adminRepository.findAllByIsActiveTrue(Sort.by(sortOrder));
            List<AdminInfoResponse> adminInfoResponses = activeAdmins.stream()
                    .map(adminMapper::toAdminInfoResponse)
                    .collect(Collectors.toList());
            return new PaginationResponse<>(adminInfoResponses, (long) adminInfoResponses.size());
        }

        int validatedPage = Math.max(0, page);
        int validatedSize = Math.max(1, size);

        Pageable pageable = PageRequest.of(validatedPage, validatedSize, Sort.by(sortOrder));
        Specification<Admin> spec = Specification.where(AdminSpecification.hasFirstnameLastname(firstnameLastname))
                .and(AdminSpecification.hasId(id))
                .and(AdminSpecification.isActive());
        Page<Admin> activeAdminsPage = adminRepository.findAll(spec, pageable);

        if (activeAdminsPage.isEmpty()) {
            return new PaginationResponse<>(
                    Collections.emptyList(),
                    0L
            );
        }

        List<AdminInfoResponse> adminInfoResponses = activeAdminsPage.getContent().stream()
                .map(adminMapper::toAdminInfoResponse)
                .collect(Collectors.toList());

        return new PaginationResponse<>(
                activeAdminsPage.getNumber(),
                activeAdminsPage.getSize(),
                activeAdminsPage.getTotalElements(),
                activeAdminsPage.getTotalPages(),
                adminInfoResponses
        );
    }

    @Override
    public ApiResponse<CountResponse> getAdminsCount() {
        List<Admin> admins = adminRepository.findAllByIsActiveTrue();
        CountResponse countResponse = new CountResponse();
        countResponse.setCount(admins.size());
        return ApiResponse.<CountResponse>builder()
                .data(countResponse)
                .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                .build();
    }

    @Override
    public ApiResponse<AdminInfoResponse> getAdminByUniqueId(String id) {
        Optional<Admin> adminOptional = adminRepository.getAdminByAdminIdAndIsActive(id, Boolean.TRUE);

        return adminOptional.map(admin -> {
            AdminInfoResponse response = adminMapper.toAdminInfoResponse(admin);
            return ApiResponse.<AdminInfoResponse>builder()
                    .data(response)
                    .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                    .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                    .build();
        }).orElseGet(() -> ApiResponse.<AdminInfoResponse>builder()
                .responseCode(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode())
                .responseMessage(ResponseMessage.RECORD_NOT_FOUND.getResponseMessage())
                .build());
    }

    @Override
    public ApiResponse<Void> updateAdmin(AdminInfoUpdateRequest request) {
        return adminRepository.getAdminByAdminIdAndIsActive(request.getAdminId(), Boolean.TRUE)
                .map(admin -> updateAdminDetails(admin, request))
                .orElseGet(() -> ApiResponse.<Void>builder()
                        .responseCode(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode())
                        .responseMessage(ResponseMessage.RECORD_NOT_FOUND.getResponseMessage())
                        .build()
                );
    }

    @Override
    public ApiResponse<String> deleteDAdminById(String id) {
        Admin admin = adminRepository.findByAdminId(id);
        if (admin == null) {
            return ApiResponse.<String>builder()
                    .responseCode(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode())
                    .responseMessage(ResponseMessage.RECORD_NOT_FOUND.getResponseMessage())
                    .build();
        }
        admin.setIsActive(false);
        adminRepository.save(admin);
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

    private ApiResponse<Void> updateAdminDetails(Admin admin, AdminInfoUpdateRequest request) {
        admin.setFirstname(request.getFirstname());
        admin.setLastname(request.getLastname());
        admin.setMobile(request.getMobile());
        admin.setEmail(request.getEmail());
        adminRepository.save(admin);

        Optional<User> user = userRepository.findByUserId(request.getAdminId());
        if (user.isPresent()) {
            if (!Objects.equals(request.getPassword(), "")) {
                user.get().setPassword(authConfig.passwordEncoder().encode(request.getPassword()));
            }
            user.get().setUserName(request.getAdminId());
            userRepository.save(user.get());
        }

        return ApiResponse.<Void>builder()
                .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                .build();
    }

    private RegisterResponse register(RegisterRequest request, Role role, Consumer<RegisterRequest> saveEntityFunction) {
        checkForDuplicate(request);

        // Create and save the User
        User user = createUser(request, role);
        User savedUser = userRepository.save(user);

        request.setUniqueId(user.getUserId());

        // Save the corresponding entity (Patient, Doctor, or Admin)
        saveEntityFunction.accept(request);

        logRegistration(role.name(), request.getMobile());
        return registerMapper.toRegisterResponse(savedUser);
    }

    private User createUser(RegisterRequest request, Role role) {
        String uniqueIdPrefix = getUniqueIdPrefixForRole(role);
        String uniqueId = uniqueIdGenerator.generateUniqueIdWithPrefix(uniqueIdPrefix);

        return User.builder()
                .userName(uniqueId)
                .userId(uniqueId)
                .password(authConfig.passwordEncoder().encode(request.getPassword()))
                .userType(role)
                .isActive(Boolean.TRUE)
                .build();
    }

    private void savePatient(RegisterRequest request) {
        Patient patient = Patient.builder()
                .mobile(request.getMobile())
                .firstname(request.getFirstName())
                .patientId(request.getUniqueId())
                .lastname(request.getLastName())
                .email(request.getEmail())
                .isActive(Boolean.TRUE)
                .build();
        patientRepository.save(patient);
    }

    private void saveDoctor(RegisterRequest request) {
        Doctor doctor = Doctor.builder()
                .mobile(request.getMobile())
                .firstname(request.getFirstName())
                .lastname(request.getLastName())
                .doctorId(request.getUniqueId())
                .email(request.getEmail())
                .mobile(request.getMobile())
                .department(request.getDepartment())
                .designation(request.getDesignation())
                .specialities(request.getSpecialities())
                .fee(request.getFee())
                .isActive(Boolean.TRUE)
                .build();
        doctorRepository.save(doctor);
    }

    private void saveAdmin(RegisterRequest request) {
        Admin admin = Admin.builder()
                .mobile(request.getMobile())
                .firstname(request.getFirstName())
                .adminId(request.getUniqueId())
                .lastname(request.getLastName())
                .email(request.getEmail())
                .isActive(Boolean.TRUE)
                .build();
        adminRepository.save(admin);
    }

    private String getUniqueIdPrefixForRole(Role role) {
        return switch (role) {
            case PATIENT -> patientPrefix;
            case DOCTOR -> doctorPrefix;
            case ADMIN -> adminPrefix;
            default -> throw new IllegalArgumentException("Invalid role");
        };
    }

    private void checkForDuplicate(RegisterRequest request) {
        if (userRepository.existsByUserName(request.getMobile())) {
            throw new InvalidRequestDataException(ResponseMessage.INVALID_MOBILE_NUMBER);
        }
    }

    private void logRegistration(String userType, String mobile) {
        logger.info("{} registration successful for mobile: {}", userType, mobile);
    }
}

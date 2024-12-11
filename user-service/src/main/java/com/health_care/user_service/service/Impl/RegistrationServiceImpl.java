package com.health_care.user_service.service.Impl;


import com.health_care.unique_id_generator.Impl.UniqueIdGeneratorImpl;
import com.health_care.user_service.common.exceptions.InvalidRequestDataException;
import com.health_care.user_service.config.AuthConfig;
import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.entity.Admin;
import com.health_care.user_service.domain.entity.Doctor;
import com.health_care.user_service.domain.entity.Patient;
import com.health_care.user_service.domain.entity.User;
import com.health_care.user_service.domain.enums.ApiResponseCode;
import com.health_care.user_service.domain.enums.ResponseMessage;
import com.health_care.user_service.domain.enums.Role;
import com.health_care.user_service.domain.mapper.AdminMapper;
import com.health_care.user_service.domain.mapper.RegisterMapper;
import com.health_care.user_service.domain.request.AdminInfoUpdateRequest;
import com.health_care.user_service.domain.request.RegisterRequest;
import com.health_care.user_service.domain.response.AdminInfoResponse;
import com.health_care.user_service.domain.response.CountResponse;
import com.health_care.user_service.domain.response.RegisterResponse;
import com.health_care.user_service.repository.AdminRepository;
import com.health_care.user_service.repository.DoctorRepository;
import com.health_care.user_service.repository.PatientRepository;
import com.health_care.user_service.repository.UserRepository;
import com.health_care.user_service.service.IRegistrationService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
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

//    @Value("${unique.id.patient.prefix}")
//    private String patientPrefix;
//
//    @Value("${unique.id.admin.prefix}")
//    private String adminPrefix;
//
//    @Value("${unique.id.doctor.prefix}")
//    private String doctorPrefix;

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
    public ApiResponse<List<AdminInfoResponse>> getAllAdminList(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc(sort)));
        Page<Admin> activeDoctorsPage = adminRepository.findAllByIsActiveTrue(pageable);

        if (activeDoctorsPage.isEmpty()) {
            return ApiResponse.<List<AdminInfoResponse>>builder()
                    .data(Collections.emptyList())
                    .responseCode(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode())
                    .responseMessage(ResponseMessage.RECORD_NOT_FOUND.getResponseMessage())
                    .build();
        }

        List<AdminInfoResponse> adminInfoResponses = activeDoctorsPage.getContent().stream()
                .map(adminMapper::toAdminInfoResponse)
                .collect(Collectors.toList());

        return ApiResponse.<List<AdminInfoResponse>>builder()
                .data(adminInfoResponses)
                .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                .build();
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

    private ApiResponse<Void> updateAdminDetails(Admin admin, AdminInfoUpdateRequest request) {
        admin.setFirstname(request.getFirstname());
        admin.setLastname(request.getLastname());
        admin.setMobile(request.getMobile());
        admin.setEmail(request.getEmail());
        adminRepository.save(admin);

        Optional<User> user = userRepository.findByUserId(request.getAdminId());
        user.get().setPassword(authConfig.passwordEncoder().encode(request.getPassword()));
        userRepository.save(user.get());

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
                .userName(request.getMobile())
                .userId(uniqueId)
                .password(authConfig.passwordEncoder().encode(request.getPassword()))
                .userType(role)
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
                .build();
        adminRepository.save(admin);
    }

    private String getUniqueIdPrefixForRole(Role role) {
        return switch (role) {
            case PATIENT -> "PT";
            case DOCTOR -> "DR";
            case ADMIN -> "AD";
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

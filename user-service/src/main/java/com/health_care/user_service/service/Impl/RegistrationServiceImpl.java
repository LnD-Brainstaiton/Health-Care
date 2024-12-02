package com.health_care.user_service.service.Impl;

import com.health_care.user_service.config.AuthConfig;
import com.health_care.user_service.domain.entity.Admin;
import com.health_care.user_service.domain.entity.Doctor;
import com.health_care.user_service.domain.entity.Patient;
import com.health_care.user_service.domain.entity.User;
import com.health_care.user_service.domain.enums.Role;
import com.health_care.user_service.domain.mapper.RegisterMapper;
import com.health_care.user_service.domain.request.RegisterRequest;
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
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@AllArgsConstructor
public class RegistrationServiceImpl implements IRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationServiceImpl.class);

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AuthConfig authConfig;
    private final RegisterMapper registerMapper;

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

    private RegisterResponse register(RegisterRequest request, Role role, Consumer<RegisterRequest> saveEntityFunction) {
        checkForDuplicate(request);

        User user = createUser(request, role);
        User savedUser = userRepository.save(user);

        saveEntityFunction.accept(request); // Save corresponding entity (Patient, Doctor, or Admin)

        logRegistration(role.name(), request.getMobile());
        return registerMapper.toRegisterResponse(savedUser);
    }

    private User createUser(RegisterRequest request, Role role) {
        return User.builder()
                .userName(request.getMobile())
                .password(authConfig.passwordEncoder().encode(request.getPassword()))
                .userType(role)
                .build();
    }

    private void savePatient(RegisterRequest request) {
        Patient patient = Patient.builder()
                .mobile(request.getMobile())
                .firstname(request.getFirstName())
                .lastname(request.getLastName())
                .email(request.getEmail())
                .build();
        patientRepository.save(patient);
    }

    private void saveDoctor(RegisterRequest request) {
        Doctor doctor = Doctor.builder()
                .mobile(request.getMobile())
                .firstname(request.getFirstName())
                .lastname(request.getLastName())
                .email(request.getEmail())
                .build();
        doctorRepository.save(doctor);
    }

    private void saveAdmin(RegisterRequest request) {
        Admin admin = Admin.builder()
                .mobile(request.getMobile())
                .firstname(request.getFirstName())
                .lastname(request.getLastName())
                .email(request.getEmail())
                .build();
        adminRepository.save(admin);
    }

    private void checkForDuplicate(RegisterRequest request) {
        if (userRepository.existsByUserName(request.getMobile())) {
            throw new IllegalArgumentException("Mobile number is already registered");
        }
    }

    private void logRegistration(String userType, String mobile) {
        logger.info("{} registration successful for mobile: {}", userType, mobile);
    }
}

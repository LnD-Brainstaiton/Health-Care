package com.health_care.user_service.service.Impl;

import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.entity.Admin;
import com.health_care.user_service.domain.entity.Doctor;
import com.health_care.user_service.domain.entity.Patient;
import com.health_care.user_service.domain.entity.User;
import com.health_care.user_service.domain.enums.*;
import com.health_care.user_service.domain.request.MobileCheckRequest;
import com.health_care.user_service.domain.response.BloodGroupResponse;
import com.health_care.user_service.domain.response.DepartmentResponse;
import com.health_care.user_service.domain.response.DesignationResponse;
import com.health_care.user_service.domain.response.GenderResponse;
import com.health_care.user_service.repository.AdminRepository;
import com.health_care.user_service.repository.DoctorRepository;
import com.health_care.user_service.repository.PatientRepository;
import com.health_care.user_service.repository.UserRepository;
import com.health_care.user_service.service.IDropdownService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DropdownServiceImpl implements IDropdownService {

    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Override
    public ApiResponse<BloodGroupResponse> getBloodGroupOptions() {
        BloodGroupResponse bloodGroupResponse = new BloodGroupResponse();
        List<String> bloodGroupOptions = BloodGroup.allBloodGroup();
        bloodGroupResponse.setBloodGroups(bloodGroupOptions);
        return new ApiResponse<>(bloodGroupResponse);
    }

    @Override
    public ApiResponse<DesignationResponse> getDesignationOptions() {
        DesignationResponse designationResponse = new DesignationResponse();
        List<String> designation = Designation.allDesignation();
        designationResponse.setDesignations(designation);
        return new ApiResponse<>(designationResponse);
    }

    @Override
    public ApiResponse<DepartmentResponse> getDepartmentOptions() {
        DepartmentResponse departmentResponse = new DepartmentResponse();
        List<String> department = Department.allDepartment();
        departmentResponse.setDepartments(department);
        return new ApiResponse<>(departmentResponse);
    }

    @Override
    public ApiResponse<GenderResponse> getGenderOptions() {
        GenderResponse genderResponse = new GenderResponse();
        List<String> gender = Gender.allGender();
        genderResponse.setGender(gender);
        return new ApiResponse<>(genderResponse);
    }

    @Override
    public ApiResponse<Boolean> checkMobile(MobileCheckRequest mobileCheckRequest) {
        boolean response = false;
        if(Objects.equals(mobileCheckRequest.getUserType(), "admin")) {
            Optional<Admin> admin = adminRepository.findByMobileAndIsActiveTrue(mobileCheckRequest.getMobile());
            if(admin.isPresent() && !Objects.equals(admin.get().getAdminId(), mobileCheckRequest.getUserId())) {
                response = true;
            }
        }
        else if(Objects.equals(mobileCheckRequest.getUserType(), "patient")) {
            Optional<Patient> admin = patientRepository.findByMobileAndIsActiveTrue(mobileCheckRequest.getMobile());
            if(admin.isPresent() && !Objects.equals(admin.get().getPatientId(), mobileCheckRequest.getUserId())) {
                response = true;
            }
        }
        else if(Objects.equals(mobileCheckRequest.getUserType(), "doctor")) {
            Optional<Doctor> admin = doctorRepository.findByMobileAndIsActiveTrue(mobileCheckRequest.getMobile());
            if(admin.isPresent() && !Objects.equals(admin.get().getDoctorId(), mobileCheckRequest.getUserId())) {
                response = true;
            }
        }
        return ApiResponse.<Boolean>builder()
                .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                .data(response)
                .build();
    }


}

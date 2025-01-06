package com.healthcare.userservice.service.Impl;

import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.entity.Admin;
import com.healthcare.userservice.domain.entity.Doctor;
import com.healthcare.userservice.domain.entity.Patient;
import com.healthcare.userservice.domain.entity.TempData;
import com.healthcare.userservice.domain.enums.*;
import com.healthcare.userservice.domain.request.MobileCheckRequest;
import com.healthcare.userservice.domain.response.*;
import com.healthcare.userservice.repository.AdminRepository;
import com.healthcare.userservice.repository.DoctorRepository;
import com.healthcare.userservice.repository.PatientRepository;
import com.healthcare.userservice.repository.TempDataRepository;
import com.healthcare.userservice.service.IDropdownService;
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
    private final TempDataRepository tempDataRepository;

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
        if (Objects.equals(mobileCheckRequest.getUserType(), "admin")) {
            Optional<Admin> admin = adminRepository.findByMobileAndIsActiveTrue(mobileCheckRequest.getMobile());
            if (admin.isPresent() && !Objects.equals(admin.get().getAdminId(), mobileCheckRequest.getUserId())) {
                response = true;
            }
        } else if (Objects.equals(mobileCheckRequest.getUserType(), "patient")) {
            Optional<Patient> admin = patientRepository.findByMobileAndIsActiveTrue(mobileCheckRequest.getMobile());
            if (admin.isPresent() && !Objects.equals(admin.get().getPatientId(), mobileCheckRequest.getUserId())) {
                response = true;
            }
        } else if (Objects.equals(mobileCheckRequest.getUserType(), "doctor")) {
            Optional<Doctor> admin = doctorRepository.findByMobileAndIsActiveTrue(mobileCheckRequest.getMobile());
            if (admin.isPresent() && !Objects.equals(admin.get().getDoctorId(), mobileCheckRequest.getUserId())) {
                response = true;
            }
        }
        return ApiResponse.<Boolean>builder()
                .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                .data(response)
                .build();
    }

    @Override
    public ApiResponse<CountResponse> pendingDoctorCount() {
        List<TempData> tempData = tempDataRepository.findAllByFeatureCodeAndIsActiveTrue("DOCTOR");
        CountResponse countResponse = new CountResponse();
        countResponse.setCount(tempData.size());
        return ApiResponse.<CountResponse>builder()
                .data(countResponse)
                .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                .build();
    }

    @Override
    public ApiResponse<CountResponse> pendingAppointmentCount() {
        List<TempData> tempData = tempDataRepository.findAllByFeatureCodeAndIsActiveTrue("APPOINTMENT");
        CountResponse countResponse = new CountResponse();
        countResponse.setCount(tempData.size());
        return ApiResponse.<CountResponse>builder()
                .data(countResponse)
                .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                .build();
    }

    @Override
    public ApiResponse<CountResponse> pendingAdminCount() {
        List<TempData> tempData = tempDataRepository.findAllByFeatureCodeAndIsActiveTrue("ADMIN");
        CountResponse countResponse = new CountResponse();
        countResponse.setCount(tempData.size());
        return ApiResponse.<CountResponse>builder()
                .data(countResponse)
                .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                .build();
    }


}

package com.healthcare.userservice.service.Impl;

import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.entity.*;
import com.healthcare.userservice.domain.enums.*;
import com.healthcare.userservice.domain.request.EmailCheckRequest;
import com.healthcare.userservice.domain.request.MobileCheckRequest;
import com.healthcare.userservice.domain.response.*;
import com.healthcare.userservice.repository.*;
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
    private final UserRepository userRepository;

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
        boolean response = true;

        if(userRepository.findByMobileNumberAndIsActiveTrue(mobileCheckRequest.getMobile()).isEmpty())
            response = false;
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

    @Override
    public ApiResponse<Boolean> checkEmail(EmailCheckRequest emailCheckRequest) {
        boolean response = true;

        if(userRepository.findByEmailAndIsActiveTrue(emailCheckRequest.getEmail()).isEmpty())
            response = false;
        return ApiResponse.<Boolean>builder()
                .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                .data(response)
                .build();
    }


}

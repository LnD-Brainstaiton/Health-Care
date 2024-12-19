package com.health_care.user_service.service.Impl;

import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.enums.BloodGroup;
import com.health_care.user_service.domain.enums.Department;
import com.health_care.user_service.domain.enums.Designation;
import com.health_care.user_service.domain.enums.Gender;
import com.health_care.user_service.domain.response.BloodGroupResponse;
import com.health_care.user_service.domain.response.DepartmentResponse;
import com.health_care.user_service.domain.response.DesignationResponse;
import com.health_care.user_service.domain.response.GenderResponse;
import com.health_care.user_service.service.IDropdownService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DropdownServiceImpl implements IDropdownService {



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


}

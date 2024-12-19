package com.healthcare.userservice.service.Impl;

import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.enums.BloodGroup;
import com.healthcare.userservice.domain.enums.Department;
import com.healthcare.userservice.domain.enums.Designation;
import com.healthcare.userservice.domain.enums.Gender;
import com.healthcare.userservice.domain.response.BloodGroupResponse;
import com.healthcare.userservice.domain.response.DepartmentResponse;
import com.healthcare.userservice.domain.response.DesignationResponse;
import com.healthcare.userservice.domain.response.GenderResponse;
import com.healthcare.userservice.service.IDropdownService;
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

package com.healthcare.userservice.service;

import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.response.BloodGroupResponse;
import com.healthcare.userservice.domain.response.DepartmentResponse;
import com.healthcare.userservice.domain.response.DesignationResponse;
import com.healthcare.userservice.domain.response.GenderResponse;

public interface IDropdownService {
    ApiResponse<BloodGroupResponse> getBloodGroupOptions();

    ApiResponse<DesignationResponse> getDesignationOptions();

    ApiResponse<DepartmentResponse> getDepartmentOptions();

    ApiResponse<GenderResponse> getGenderOptions();
}

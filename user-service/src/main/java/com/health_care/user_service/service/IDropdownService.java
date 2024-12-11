package com.health_care.user_service.service;

import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.response.BloodGroupResponse;
import com.health_care.user_service.domain.response.DepartmentResponse;
import com.health_care.user_service.domain.response.DesignationResponse;
import com.health_care.user_service.domain.response.GenderResponse;
import org.springframework.stereotype.Service;

public interface IDropdownService {
    ApiResponse<BloodGroupResponse> getBloodGroupOptions();

    ApiResponse<DesignationResponse> getDesignationOptions();

    ApiResponse<DepartmentResponse> getDepartmentOptions();

    ApiResponse<GenderResponse> getGenderOptions();
}

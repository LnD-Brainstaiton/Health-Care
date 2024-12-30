package com.health_care.user_service.service;

import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.request.MobileCheckRequest;
import com.health_care.user_service.domain.response.*;
import org.springframework.stereotype.Service;

public interface IDropdownService {
    ApiResponse<BloodGroupResponse> getBloodGroupOptions();

    ApiResponse<DesignationResponse> getDesignationOptions();

    ApiResponse<DepartmentResponse> getDepartmentOptions();

    ApiResponse<GenderResponse> getGenderOptions();

    ApiResponse<Boolean> checkMobile(MobileCheckRequest request);

    ApiResponse<CountResponse> pendingDoctorCount();

    ApiResponse<CountResponse> pendingAppointmentCount();

    ApiResponse<CountResponse> pendingAdminCount();
}

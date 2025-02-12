package com.healthcare.userservice.service;

import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.request.EmailCheckRequest;
import com.healthcare.userservice.domain.request.MobileCheckRequest;
import com.healthcare.userservice.domain.response.*;

public interface IDropdownService {
    ApiResponse<BloodGroupResponse> getBloodGroupOptions();

    ApiResponse<DesignationResponse> getDesignationOptions();

    ApiResponse<DepartmentResponse> getDepartmentOptions();

    ApiResponse<GenderResponse> getGenderOptions();

    ApiResponse<Boolean> checkMobile(MobileCheckRequest request);

    ApiResponse<CountResponse> pendingDoctorCount();

    ApiResponse<CountResponse> pendingAppointmentCount();

    ApiResponse<CountResponse> pendingAdminCount();

    ApiResponse<Boolean> checkEmail(EmailCheckRequest emailCheckRequest);
}

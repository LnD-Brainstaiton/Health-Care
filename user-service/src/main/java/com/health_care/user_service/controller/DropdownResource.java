package com.health_care.user_service.controller;

import com.health_care.user_service.common.utils.AppUtils;
import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.request.MobileCheckRequest;
import com.health_care.user_service.domain.response.*;
import com.health_care.user_service.service.IDropdownService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AppUtils.BASE_URL)
@AllArgsConstructor
public class DropdownResource {

    private final IDropdownService dropdownService;

    @GetMapping("/blood-group-options")
    public ApiResponse<BloodGroupResponse> bloodGroupOptions() {
        return dropdownService.getBloodGroupOptions();
    }

    @GetMapping("/designation-options")
    public ApiResponse<DesignationResponse> DesignationOptions() {
        return dropdownService.getDesignationOptions();
    }

    @GetMapping("/department-options")
    public ApiResponse<DepartmentResponse> DepartmentOptions() {
        return dropdownService.getDepartmentOptions();
    }

    @GetMapping("/gender-options")
    public ApiResponse<GenderResponse> GenderOptions() {
        return dropdownService.getGenderOptions();
    }

    @PostMapping("/check-mobile")
    public ApiResponse<Boolean> checkMobile(@RequestBody MobileCheckRequest mobileCheckRequest) {
        return dropdownService.checkMobile(mobileCheckRequest);
    }

    @GetMapping("/pending-doctor-count")
    public ApiResponse<CountResponse> pendingDoctorCount() {
        return dropdownService.pendingDoctorCount();
    }

    @GetMapping("/pending-appointment-count")
    public ApiResponse<CountResponse> pendingAppointmentCount() {
        return dropdownService.pendingAppointmentCount();
    }

    @GetMapping("/pending-admin-count")
    public ApiResponse<CountResponse> pendingAdminCount() {
        return dropdownService.pendingAdminCount();
    }

}

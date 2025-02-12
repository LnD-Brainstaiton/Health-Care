package com.healthcare.userservice.presenter.rest.api;

import com.healthcare.userservice.common.utils.AppUtils;
import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.request.EmailCheckRequest;
import com.healthcare.userservice.domain.request.MobileCheckRequest;
import com.healthcare.userservice.domain.response.*;
import com.healthcare.userservice.service.IDropdownService;
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

    @PostMapping("/check-email")
    public ApiResponse<Boolean> checkEmail(@RequestBody EmailCheckRequest emailCheckRequest) {
        return dropdownService.checkEmail(emailCheckRequest);
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

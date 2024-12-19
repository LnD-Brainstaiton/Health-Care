package com.health_care.user_service.controller;

import com.health_care.user_service.common.utils.AppUtils;
import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.response.BloodGroupResponse;
import com.health_care.user_service.domain.response.DepartmentResponse;
import com.health_care.user_service.domain.response.DesignationResponse;
import com.health_care.user_service.domain.response.GenderResponse;
import com.health_care.user_service.service.IDropdownService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}

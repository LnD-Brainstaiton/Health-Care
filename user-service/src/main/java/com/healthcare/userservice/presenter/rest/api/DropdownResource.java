package com.healthcare.userservice.presenter.rest.api;

import com.healthcare.userservice.domain.response.BloodGroupResponse;
import com.healthcare.userservice.domain.response.DepartmentResponse;
import com.healthcare.userservice.domain.response.DesignationResponse;
import com.healthcare.userservice.domain.response.GenderResponse;
import com.healthcare.userservice.common.utils.AppUtils;
import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.service.IDropdownService;
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

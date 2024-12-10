package com.health_care.user_service.controller;

import com.health_care.user_service.common.utils.AppUtils;
import com.health_care.user_service.common.utils.ResponseUtils;
import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.enums.ResponseMessage;
import com.health_care.user_service.domain.request.RegisterRequest;
import com.health_care.user_service.domain.response.AdminInfoResponse;
import com.health_care.user_service.domain.response.CountResponse;
import com.health_care.user_service.domain.response.DoctorInfoResponse;
import com.health_care.user_service.domain.response.RegisterResponse;
import com.health_care.user_service.service.IRegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AppUtils.BASE_URL)
@AllArgsConstructor
public class AdminResource {

    private final IRegistrationService registrationService;

    @PostMapping("/admin/create")
    public ApiResponse<RegisterResponse> register(@RequestBody RegisterRequest admin) {
        RegisterResponse response = registrationService.registerAdmin(admin);
        return ResponseUtils.createResponseObject(ResponseMessage.OPERATION_SUCCESSFUL, response);
    }

    @GetMapping("/admin/{id}")
    public ApiResponse<AdminInfoResponse> getAdminByMobile(@PathVariable String id) {
        ApiResponse<AdminInfoResponse> response = registrationService.getAdminByMobile(id);
        return response;
    }

    @GetMapping("/admin/all")
    public ApiResponse<List<AdminInfoResponse>> getAllAdminList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstname") String sort) {
        ApiResponse<List<AdminInfoResponse>> response = registrationService.getAllAdminList(page, size, sort);
        return response;
    }

    @GetMapping("/admin/count")
    public ApiResponse<CountResponse> getAdminsCount(){
        ApiResponse<CountResponse> response = registrationService.getAdminsCount();
        return response;
    }


}

package com.healthcare.userservice.presenter.rest.api;

import com.healthcare.userservice.common.utils.AppUtils;
import com.healthcare.userservice.common.utils.ResponseUtils;
import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.enums.ApiResponseCode;
import com.healthcare.userservice.domain.enums.ResponseMessage;
import com.healthcare.userservice.domain.request.AdminInfoUpdateRequest;
import com.healthcare.userservice.domain.request.RegisterRequest;
import com.healthcare.userservice.domain.response.AdminInfoResponse;
import com.healthcare.userservice.domain.response.CountResponse;
import com.healthcare.userservice.domain.response.PaginationResponse;
import com.healthcare.userservice.domain.response.RegisterResponse;
import com.healthcare.userservice.service.IRegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


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
    public ApiResponse<AdminInfoResponse> getAdminByUniqueId(@PathVariable String id) {
        ApiResponse<AdminInfoResponse> response = registrationService.getAdminByUniqueId(id);
        return response;
    }

    @PutMapping("/admin/update")
    public ApiResponse<Void> updateAdmin(@RequestBody AdminInfoUpdateRequest request) {
        ApiResponse<Void> response = registrationService.updateAdmin(request);
        return response;
    }

    @GetMapping("/admin/all")
    public ApiResponse<PaginationResponse<AdminInfoResponse>> getAllAdminList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstname") String sort,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String firstnameLastname) {
        PaginationResponse<AdminInfoResponse> response = registrationService.getAllAdminList(page, size, sort, firstnameLastname, id);
        if (response.getData() == null || response.getData().isEmpty()) {
            return ApiResponse.<PaginationResponse<AdminInfoResponse>>builder()
                    .data(response) // Empty pagination response
                    .responseCode(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode())
                    .responseMessage(ResponseMessage.RECORD_NOT_FOUND.getResponseMessage())
                    .build();
        }

        return ApiResponse.<PaginationResponse<AdminInfoResponse>>builder()
                .data(response)
                .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                .build();
    }

    @GetMapping("/admin/count")
    public ApiResponse<CountResponse> getAdminsCount() {
        ApiResponse<CountResponse> response = registrationService.getAdminsCount();
        return response;
    }

    @DeleteMapping("/admin/{id}")
    public ApiResponse<String> deleteAdminById(@PathVariable String id) {
        ApiResponse<String> response = registrationService.deleteDAdminById(id);
        return response;
    }

}

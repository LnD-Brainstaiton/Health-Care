package com.healthcare.userservice.presenter.rest.api;

import com.healthcare.userservice.common.utils.AppUtils;
import com.healthcare.userservice.common.utils.ResponseUtils;
import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.enums.ApiResponseCode;
import com.healthcare.userservice.domain.enums.ResponseMessage;
import com.healthcare.userservice.domain.request.PatientInfoUpdateRequest;
import com.healthcare.userservice.domain.request.RegisterRequest;
import com.healthcare.userservice.domain.response.CountResponse;
import com.healthcare.userservice.domain.response.PaginationResponse;
import com.healthcare.userservice.domain.response.PatientInfoResponse;
import com.healthcare.userservice.domain.response.RegisterResponse;
import com.healthcare.userservice.service.IPatientService;
import com.healthcare.userservice.service.IRegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AppUtils.BASE_URL)
@AllArgsConstructor
public class PatientResource {

    private final IRegistrationService registrationService;
    private final IPatientService patientService;

    @PostMapping("/patient/register")
    public ApiResponse<RegisterResponse> register(@RequestBody RegisterRequest user) {
        RegisterResponse response = registrationService.registerUser(user);
        return ResponseUtils.createResponseObject(ResponseMessage.OPERATION_SUCCESSFUL, response);
    }

    @PutMapping("/patient/update")
    public ApiResponse<Void> updatePatient(@RequestBody PatientInfoUpdateRequest request) {
        ApiResponse<Void> response = patientService.updatePatient(request);
        return response;
    }

    @GetMapping("/patient/{id}")
    public ApiResponse<PatientInfoResponse> getPatient(@PathVariable String id) {
        ApiResponse<PatientInfoResponse> response = patientService.getPatientById(id);
        return response;
    }

    @DeleteMapping("/patient/{id}")
    public ApiResponse<String> deletePatient(@PathVariable String id) {
        ApiResponse<String> response = patientService.deletePatient(id);
        return response;
    }

    @GetMapping("/patient/count")
    public ApiResponse<CountResponse> getAPatientsCount() {
        ApiResponse<CountResponse> response = patientService.getAPatientsCount();
        return response;
    }

    @GetMapping("/patient/all")
    public ApiResponse<PaginationResponse<PatientInfoResponse>> getAllPatientList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstname") String sort,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String firstnameLastname) {
        PaginationResponse<PatientInfoResponse> response = patientService.getAllPatientList(page, size, sort, firstnameLastname, id);
        if (response.getData() == null || response.getData().isEmpty()) {
            return ApiResponse.<PaginationResponse<PatientInfoResponse>>builder()
                    .data(response) // Empty pagination response
                    .responseCode(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode())
                    .responseMessage(ResponseMessage.RECORD_NOT_FOUND.getResponseMessage())
                    .build();
        }

        return ApiResponse.<PaginationResponse<PatientInfoResponse>>builder()
                .data(response)
                .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                .build();
    }
}

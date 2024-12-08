package com.health_care.user_service.controller;

import com.health_care.user_service.common.utils.AppUtils;
import com.health_care.user_service.common.utils.ResponseUtils;
import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.entity.Patient;
import com.health_care.user_service.domain.enums.ResponseMessage;
import com.health_care.user_service.domain.request.PatientInfoUpdateRequest;
import com.health_care.user_service.domain.request.RegisterRequest;
import com.health_care.user_service.domain.response.CountResponse;
import com.health_care.user_service.domain.response.DoctorInfoResponse;
import com.health_care.user_service.domain.response.PatientInfoResponse;
import com.health_care.user_service.domain.response.RegisterResponse;
import com.health_care.user_service.service.IPatientService;
import com.health_care.user_service.service.IRegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/patient/update")
    public ApiResponse<Void> updatePatient(@RequestBody PatientInfoUpdateRequest request) {
        ApiResponse<Void> response = patientService.updatePatient(request);
        return response;
    }

    @GetMapping("/patient/{id}")
    public ApiResponse<Patient> getPatient(@PathVariable String id) {
        ApiResponse<Patient> response = patientService.getPatientById(id);
        return response;
    }

    @GetMapping("/patient/count")
    public ApiResponse<CountResponse> getAPatientsCount(){
        ApiResponse<CountResponse> response = patientService.getAPatientsCount();
        return response;
    }

    @GetMapping("/patient/all")
    public ApiResponse<List<PatientInfoResponse>> getAllPatientList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstname") String sort) {
        ApiResponse<List<PatientInfoResponse>> response = patientService.getAllPatientList(page,size,sort);
        return response;
    }
}

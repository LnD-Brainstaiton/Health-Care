package com.health_care.user_service.controller;

import com.health_care.user_service.common.utils.AppUtils;
import com.health_care.user_service.common.utils.ResponseUtils;
import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.enums.ApiResponseCode;
import com.health_care.user_service.domain.enums.ResponseMessage;
import com.health_care.user_service.domain.request.DoctorInfoUpdateRequest;
import com.health_care.user_service.domain.request.RegisterRequest;
import com.health_care.user_service.domain.response.CountResponse;
import com.health_care.user_service.domain.response.DoctorInfoResponse;
import com.health_care.user_service.domain.response.PaginationResponse;
import com.health_care.user_service.domain.response.RegisterResponse;
import com.health_care.user_service.service.IDoctorService;
import com.health_care.user_service.service.IRegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AppUtils.BASE_URL)
@AllArgsConstructor
public class DoctorResource {

    private final IRegistrationService registrationService;
    private final IDoctorService doctorService;

    @PostMapping("/doctor/create")
    public ApiResponse<RegisterResponse> registerDoctor(@RequestBody RegisterRequest doctor) {
        RegisterResponse response = registrationService.registerDoctor(doctor);
        return ResponseUtils.createResponseObject(ResponseMessage.OPERATION_SUCCESSFUL, response);
    }

    @PutMapping("/doctor/update")
    public ApiResponse<Void> updateDoctor(@RequestBody DoctorInfoUpdateRequest request) {
        ApiResponse<Void> response = doctorService.updateDoctor(request);
        return response;
    }

    @GetMapping("/doctor/{id}")
    public ApiResponse<DoctorInfoResponse> getDoctorByMobile(@PathVariable String id) {
        ApiResponse<DoctorInfoResponse> response = doctorService.getDoctorById(id);
        return response;
    }


    @GetMapping("/doctor/all")
    public ApiResponse<PaginationResponse<DoctorInfoResponse>> getAllDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "department") String sort,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String firstnameLastname,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String designation,
            @RequestParam(required = false) String gender) {
        PaginationResponse<DoctorInfoResponse> response = doctorService.getAllDoctorInfo(page, size, sort, firstnameLastname, id, department, designation, gender);

        if (response.getData() == null || response.getData().isEmpty()) {
            return ApiResponse.<PaginationResponse<DoctorInfoResponse>>builder()
                    .data(response) // Empty pagination response
                    .responseCode(ApiResponseCode.RECORD_NOT_FOUND.getResponseCode())
                    .responseMessage(ResponseMessage.RECORD_NOT_FOUND.getResponseMessage())
                    .build();
        }

        return ApiResponse.<PaginationResponse<DoctorInfoResponse>>builder()
                .data(response)
                .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                .build();
    }


    @GetMapping("/doctor/count")
    public ApiResponse<CountResponse> getDoctorsCount(){
        ApiResponse<CountResponse> response = doctorService.getDoctorsCount();
        return response;
    }



    @DeleteMapping("/doctor/{id}")
    public  ApiResponse<String> deleteDoctorById(@PathVariable String id){
        ApiResponse<String> response = doctorService.deleteDoctorById(id);
        return response;
    }


}

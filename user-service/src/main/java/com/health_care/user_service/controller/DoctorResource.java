package com.health_care.user_service.controller;

import com.health_care.user_service.common.utils.AppUtils;
import com.health_care.user_service.common.utils.ResponseUtils;
import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.enums.ApiResponseCode;
import com.health_care.user_service.domain.enums.ResponseMessage;
import com.health_care.user_service.domain.request.DoctorInfoUpdateRequest;
import com.health_care.user_service.domain.request.RegisterRequest;
import com.health_care.user_service.domain.request.TimeSlotRequest;
import com.health_care.user_service.domain.response.*;
import com.health_care.user_service.service.IDoctorService;
import com.health_care.user_service.service.IRegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/doctor/update")
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
            @RequestParam(defaultValue = "department") String sort) {
        PaginationResponse<DoctorInfoResponse> response = doctorService.getAllDoctorInfo(page, size, sort);

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

    @PostMapping("/doctor/time-slot")
    public ApiResponse<TimeSlotResponse> getDoctorTimeSlot(@RequestBody TimeSlotRequest request) {
        return ResponseUtils.createResponseObject(ResponseMessage.OPERATION_SUCCESSFUL, doctorService.getTimeSlotList(request));
    }

}

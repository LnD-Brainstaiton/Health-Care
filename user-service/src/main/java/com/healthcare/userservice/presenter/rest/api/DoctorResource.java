package com.healthcare.userservice.presenter.rest.api;

import com.healthcare.userservice.common.utils.AppUtils;
import com.healthcare.userservice.common.utils.ResponseUtils;
import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.enums.ApiResponseCode;
import com.healthcare.userservice.domain.enums.ResponseMessage;
import com.healthcare.userservice.domain.request.BmdcValidationRequest;
import com.healthcare.userservice.domain.request.DoctorInfoUpdateRequest;
import com.healthcare.userservice.domain.request.DoctorProfessionalInfoRequest;
import com.healthcare.userservice.domain.request.DoctorVacationRequest;
import com.healthcare.userservice.domain.request.RegisterRequest;
import com.healthcare.userservice.domain.request.TimeSlotRequest;
import com.healthcare.userservice.domain.response.*;
import com.healthcare.userservice.service.IDoctorService;
import com.healthcare.userservice.service.IRegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AppUtils.BASE_URL)
@AllArgsConstructor
public class DoctorResource {

    private final IRegistrationService registrationService;
    private final IDoctorService doctorService;

    @PostMapping("/doctor/init/request")
    public ApiResponse<RegisterResponse> registerDoctorLeve1(@RequestBody RegisterRequest doctor) {
        RegisterResponse response = registrationService.registerDoctorLeve1(doctor);
        return ResponseUtils.createResponseObject(ResponseMessage.OPERATION_SUCCESSFUL, response);
    }

    @PostMapping("/doctor/create/request")
    public ApiResponse<RegisterResponse> registerDoctor(@RequestBody DoctorProfessionalInfoRequest doctor) {
        RegisterResponse response = registrationService.registerDoctor(doctor);
        return ResponseUtils.createResponseObject(ResponseMessage.OPERATION_SUCCESSFUL, response);
    }

    @PutMapping("/doctor/update")
    public ApiResponse<Void> updateDoctor(@RequestBody DoctorInfoUpdateRequest request) {
        return doctorService.updateDoctor(request);
    }

    @GetMapping("/doctor/{id}")
    public ApiResponse<DoctorInfoResponse> getDoctorById(@PathVariable String id) {
        return doctorService.getDoctorById(id);
    }

    @PutMapping("/doctor/vacation/data")
    public ApiResponse<Void> updateDoctor(@RequestBody DoctorVacationRequest request) {
        return doctorService.vacationRequest(request);
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
    public ApiResponse<CountResponse> getDoctorsCount() {
        return doctorService.getDoctorsCount();
    }

    @DeleteMapping("/doctor/{id}")
    public ApiResponse<String> deleteDoctorById(@PathVariable String id) {
        return doctorService.deleteDoctorById(id);
    }

    @PostMapping("/doctor/time-slot")
    public ApiResponse<TimeSlotResponse> getDoctorTimeSlot(@RequestBody TimeSlotRequest request) {
        return ResponseUtils.createResponseObject(ResponseMessage.OPERATION_SUCCESSFUL, doctorService.getTimeSlotList(request));
    }

    @GetMapping("/doctor/get/captcha")
    public ApiResponse<String> getCaptcha() {
        return doctorService.getCaptcha();
    }

    @PostMapping("/doctor/validate/registration")
    public ApiResponse<String> validateRegistration(@RequestBody BmdcValidationRequest request){
        return doctorService.validateRegistration(request);
    }

}

package com.healthcare.userservice.presenter.rest.external;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.request.TimeSlotRequest;
import com.healthcare.userservice.domain.response.AppointmentResponse;
import com.healthcare.userservice.domain.response.PaginationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalTime;
import java.util.List;

@FeignClient(name = "APPOINTMENT-SERVICE", contextId = "appointment-service", path = "/api/v1/appointment")
public interface AppointmentClient {

    @PostMapping("/time-slot")
    ApiResponse<List<LocalTime>> getTimeSlot(@RequestBody TimeSlotRequest request);


    @GetMapping("/list")
    ApiResponse<PaginationResponse<AppointmentResponse>> listAppointments(
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "updatedAt") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) String doctorId,
            @RequestParam(required = false) String patientId,
            @RequestParam(required = false) String appointmentId,
            @RequestParam(required = false) @JsonFormat(pattern = "yyyy-MM-dd") String date,
            @RequestParam(required = false) @JsonFormat(pattern = "HH:mm:ss") String time
    );
}
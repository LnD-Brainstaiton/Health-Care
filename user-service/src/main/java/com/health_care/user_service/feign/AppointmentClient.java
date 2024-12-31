package com.health_care.user_service.feign;

import com.health_care.user_service.domain.common.ApiResponse;
import com.health_care.user_service.domain.request.TimeSlotRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalTime;
import java.util.List;

@FeignClient(name = "APPOINTMENT-SERVICE", contextId = "appointment-service", path = "/api/v1/appointment")
public interface AppointmentClient {

    @PostMapping("/time-slot")
    ApiResponse<List<LocalTime>> getTimeSlot(@RequestBody TimeSlotRequest request);
}
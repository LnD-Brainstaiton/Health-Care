package com.health.care.appointment.service.api;

import com.health.care.appointment.service.common.utils.ResponseUtils;
import com.health.care.appointment.service.domain.common.ApiResponse;
import com.health.care.appointment.service.domain.common.AppUtils;
import com.health.care.appointment.service.domain.enums.ResponseMessage;
import com.health.care.appointment.service.domain.request.AppointmentRequest;
import com.health.care.appointment.service.domain.response.AppointmentResponse;
import com.health.care.appointment.service.service.IAppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppUtils.BASE_URL)
@RequiredArgsConstructor
public class AppointmentResource {

    private final IAppointmentService appointmentService;

    @PostMapping("/create/appointment")
    public ApiResponse<AppointmentResponse> requestAppointment(@RequestBody AppointmentRequest appointmentRequest) {
        AppointmentResponse response = appointmentService.requestAppointment(appointmentRequest);
    return ResponseUtils.createResponseObject(ResponseMessage.OPERATION_SUCCESSFUL,response);
    }
}



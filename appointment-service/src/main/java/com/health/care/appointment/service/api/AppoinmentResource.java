package com.health.care.appointment.service.api;

import com.health.care.appointment.service.common.utils.AppUtils;
import com.health.care.appointment.service.common.utils.ResponseUtils;
import com.health.care.appointment.service.domain.common.ApiResponse;
import com.health.care.appointment.service.domain.enums.ResponseMessage;
import com.health.care.appointment.service.domain.request.CreateAppointmentRequest;
import com.health.care.appointment.service.service.IAppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppUtils.BASE_URL)
public class AppoinmentResource {

    private final IAppointmentService appointmentService;

    @PostMapping("/create")
    public ApiResponse<Void> createAppointment(@RequestBody CreateAppointmentRequest request) {
        return ResponseUtils.createResponseObject(ResponseMessage.OPERATION_SUCCESSFUL, appointmentService.addAppointment(request));
    }
}

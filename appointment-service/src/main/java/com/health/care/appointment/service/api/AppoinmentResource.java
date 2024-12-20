package com.health.care.appointment.service.api;

import com.health.care.appointment.service.common.utils.AppUtils;
import com.health.care.appointment.service.domain.common.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppUtils.BASE_URL)
public class AppoinmentResource {

    @PostMapping("/create")
    public ApiResponse<Void> createAppointment() {}
}

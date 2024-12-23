package com.health.care.appointment.service.service;

import com.health.care.appointment.service.domain.request.CreateAppointmentRequest;
import com.health.care.appointment.service.domain.request.UpdateAppointmentRequest;

public interface IAppointmentService {

    Void addAppointment(CreateAppointmentRequest request);
    Void updateAppointment(UpdateAppointmentRequest request);
}

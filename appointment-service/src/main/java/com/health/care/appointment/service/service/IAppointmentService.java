package com.health.care.appointment.service.service;

import com.health.care.appointment.service.domain.request.CreateAppointmentRequest;

public interface IAppointmentService {

    Void addAppointment(CreateAppointmentRequest request);
}

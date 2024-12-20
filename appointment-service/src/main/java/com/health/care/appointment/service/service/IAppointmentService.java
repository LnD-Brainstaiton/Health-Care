package com.health.care.appointment.service.service;

import com.health.care.appointment.service.domain.request.AppointmentRequest;
import com.health.care.appointment.service.domain.response.AppointmentResponse;

public interface IAppointmentService {
    AppointmentResponse requestAppointment(AppointmentRequest appointmentRequest);
}

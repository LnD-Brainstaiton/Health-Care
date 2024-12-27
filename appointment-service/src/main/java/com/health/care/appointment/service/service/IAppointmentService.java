package com.health.care.appointment.service.service;

import com.health.care.appointment.service.domain.request.CreateAppointmentRequest;
import com.health.care.appointment.service.domain.request.UpdateAppointmentRequest;
import com.health.care.appointment.service.domain.response.AppointmentResponse;
import com.health.care.appointment.service.domain.response.PaginationResponse;

public interface IAppointmentService {

    Void addAppointment(CreateAppointmentRequest request);
    Void updateAppointment(UpdateAppointmentRequest request);
    PaginationResponse<AppointmentResponse> listOfAppointments(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortOrder,
            String doctorId,
            String patientId,
            String date,
            String time
    );
}

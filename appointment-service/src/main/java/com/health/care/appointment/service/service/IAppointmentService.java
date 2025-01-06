package com.health.care.appointment.service.service;

import com.health.care.appointment.service.domain.common.ApiResponse;
import com.health.care.appointment.service.domain.request.CreateAppointmentRequest;
import com.health.care.appointment.service.domain.request.TimeSlotRequest;
import com.health.care.appointment.service.domain.request.UpdateAppointmentRequest;
import com.health.care.appointment.service.domain.response.AppointmentResponse;
import com.health.care.appointment.service.domain.response.CountResponse;
import com.health.care.appointment.service.domain.response.PaginationResponse;

import java.time.LocalTime;
import java.util.List;

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
            String appointmentId,
            String date,
            String time
    );

    ApiResponse<CountResponse> getDoctorsUpcomingAppointmentCount(String doctorId,String date, String time);
    List<LocalTime> getAppointedTimeSlot(TimeSlotRequest request);
}

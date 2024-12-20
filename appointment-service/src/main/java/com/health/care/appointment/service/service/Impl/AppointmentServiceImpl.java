package com.health.care.appointment.service.service.Impl;

import com.health.care.appointment.service.domain.entity.Appointment;
import com.health.care.appointment.service.domain.enums.Status;
import com.health.care.appointment.service.domain.request.AppointmentRequest;
import com.health.care.appointment.service.domain.response.AppointmentResponse;
import com.health.care.appointment.service.repository.AppointmentRepository;
import com.health.care.appointment.service.service.IAppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements IAppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public AppointmentResponse requestAppointment(AppointmentRequest appointmentRequest) {
        Appointment savedAppointment = saveAppointment(appointmentRequest);
        return mapToResponse(savedAppointment);
    }

    private Appointment saveAppointment(AppointmentRequest appointmentRequest) {
        Appointment appointment = mapToEntity(appointmentRequest);
        return appointmentRepository.save(appointment);
    }
    private Appointment mapToEntity(AppointmentRequest request) {
        return Appointment.builder()
                .doctorId(request.getDoctorId())
                .appointmentDate(request.getAppointmentDate())
                .appointmentTime(request.getAppointmentTime())
                .status(Status.PENDING)
                .isPaymentDone(false)
                .patientName(request.getPatientName())
                .patientAge(request.getPatientAge())
                .patientGender(request.getPatientGender())
                .patientId(request.getPatientId())
                .patientContactNo(request.getPatientContactNo())
                .fee(request.getFee())
                .build();
    }

    private AppointmentResponse mapToResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .doctorId(appointment.getDoctorId())
                .appointmentNo(appointment.getAppointmentNo())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .status(appointment.getStatus())
                .isPaymentDone(appointment.getIsPaymentDone())
                .patientName(appointment.getPatientName())
                .patientAge(appointment.getPatientAge())
                .patientGender(appointment.getPatientGender())
                .patientId(appointment.getPatientId())
                .patientContactNo(appointment.getPatientContactNo())
                .fee(appointment.getFee())
                .approvedBy(appointment.getApprovedBy())
                .approvedTime(appointment.getApprovedTime())
                .createdAt(appointment.getCreatedAt())
                .updatedAt(appointment.getUpdatedAt())
                .build();
    }
}

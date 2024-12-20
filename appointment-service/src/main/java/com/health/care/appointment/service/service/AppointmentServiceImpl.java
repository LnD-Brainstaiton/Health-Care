package com.health.care.appointment.service.service;

import com.health.care.appointment.service.common.exceptions.InvalidRequestDataException;
import com.health.care.appointment.service.domain.entity.Appointment;
import com.health.care.appointment.service.domain.enums.ResponseMessage;
import com.health.care.appointment.service.domain.request.CreateAppointmentRequest;
import com.health.care.appointment.service.repository.AppointmentRepository;
import com.health_care.id.generator.Api.UniqueIdGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements IAppointmentService{

    private final AppointmentRepository appointmentRepository;
    private final UniqueIdGenerator uniqueIdGenerator;

    @Override
    public Void addAppointment(CreateAppointmentRequest request) {
        validateAppointmentRequest(request);

        Appointment appointment = new Appointment();
        appointment.setDoctorId(request.getDoctorId());
        appointment.setPatientId(request.getPatientId());
        appointment.setAppointmentNo(uniqueIdGenerator.generateUniqueIdWithPrefix("AP"));
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setPatientAge(request.getPatientAge());
        appointment.setPatientName(request.getPatientName());
        appointment.setPatientGender(request.getPatientGender());
        appointment.setPatientContactNo(request.getPatientContactNo());
        appointment.setFee(request.getFee());
        appointment.setIsPaymentDone(true);
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setCreatedBy("System");

        appointmentRepository.save(appointment);
        return null;
    }


    private void validateAppointmentRequest(CreateAppointmentRequest request) {
        if(Objects.isNull(request) ||
                StringUtils.isBlank(request.getDoctorId()) ||
                StringUtils.isBlank(request.getPatientId()) ||
                StringUtils.isBlank(request.getPatientName()) ||
                StringUtils.isBlank(request.getPatientContactNo()) ||
                Objects.isNull(request.getPatientGender()) ||
                Objects.isNull(request.getFee()) ||
                Objects.isNull(request.getPatientAge()) ||
                Objects.isNull(request.getAppointmentDate()) ||
                Objects.isNull(request.getAppointmentTime())) {

            throw new InvalidRequestDataException(ResponseMessage.INVALID_REQUEST_DATA);
        }
    }
}

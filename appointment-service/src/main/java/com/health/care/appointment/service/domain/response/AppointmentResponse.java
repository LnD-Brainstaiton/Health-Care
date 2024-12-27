package com.health.care.appointment.service.domain.response;

import com.health.care.appointment.service.domain.entity.Appointment;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class AppointmentResponse implements Serializable {

    private Long id;
    private String doctorId;
    private String appointmentNo;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private Boolean isPaymentDone;
    private String patientName;
    private Integer patientAge;
    private String patientGender;
    private String patientId;
    private String patientContactNo;
    private BigDecimal fee;
    private String reason;

    public static AppointmentResponse from(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .doctorId(appointment.getDoctorId())
                .appointmentNo(appointment.getAppointmentNo())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .isPaymentDone(appointment.getIsPaymentDone())
                .patientName(appointment.getPatientName())
                .patientAge(appointment.getPatientAge())
                .patientGender(String.valueOf(appointment.getPatientGender()))
                .patientId(appointment.getPatientId())
                .patientContactNo(appointment.getPatientContactNo())
                .fee(appointment.getFee())
                .reason(appointment.getReason())
                .build();
    }
}

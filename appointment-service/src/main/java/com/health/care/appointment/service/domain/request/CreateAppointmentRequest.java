package com.health.care.appointment.service.domain.request;

import com.health.care.appointment.service.domain.enums.PatientGender;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
@Data
public class CreateAppointmentRequest implements Serializable {

    private String doctorId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String patientName;
    private Integer patientAge;
    private PatientGender patientGender;
    private String patientId;
    private String patientContactNo;
    private BigDecimal fee;
    private String appointmentNo;
    private String reason;

}

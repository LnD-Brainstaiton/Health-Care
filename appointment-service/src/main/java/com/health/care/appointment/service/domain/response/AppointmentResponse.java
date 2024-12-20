package com.health.care.appointment.service.domain.response;

import com.health.care.appointment.service.domain.enums.PatientGender;
import com.health.care.appointment.service.domain.enums.Status;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class AppointmentResponse {

    private Long id;

    private String doctorId;

    private String appointmentNo;

    private LocalDate appointmentDate;

    private LocalTime appointmentTime;

    private Status status;

    private Boolean isPaymentDone;

    private String patientName;

    private Integer patientAge;

    private PatientGender patientGender;

    private String patientId;

    private String patientContactNo;

    private BigDecimal fee;

    private String approvedBy;

    private LocalDateTime approvedTime;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

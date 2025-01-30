package com.healthcare.userservice.domain.response;

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
}

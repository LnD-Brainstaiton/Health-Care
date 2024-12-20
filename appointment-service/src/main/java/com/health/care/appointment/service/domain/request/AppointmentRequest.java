package com.health.care.appointment.service.domain.request;

import com.health.care.appointment.service.domain.enums.PatientGender;
import com.health.care.appointment.service.domain.enums.Status;
import com.health.care.appointment.service.domain.response.AppointmentResponse;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentRequest {

    @NotBlank(message = "Doctor ID cannot be blank")
    private String doctorId;

    private String appointmentNo;

    @NotNull(message = "Appointment date is required")
    private LocalDate appointmentDate;

    @NotNull(message = "Appointment time is required")
    private LocalTime appointmentTime;

    @NotNull(message = "Status is required")
    private Status status;

    @NotNull(message = "Payment status is required")
    private Boolean isPaymentDone;

    @NotBlank(message = "Patient name cannot be blank")
    private String patientName;

    @Min(value = 0, message = "Patient age must be positive")
    private Integer patientAge;

    @NotNull(message = "Patient gender is required")
    private PatientGender patientGender;

    @NotBlank(message = "Patient ID cannot be blank")
    private String patientId;

    @NotBlank(message = "Patient contact number cannot be blank")
    @Pattern(regexp = "^[0-9]{10}$", message = "Contact number must be 10 digits")
    private String patientContactNo;

    @DecimalMin(value = "0.0", inclusive = false, message = "Fee must be greater than zero")
    private BigDecimal fee;

}

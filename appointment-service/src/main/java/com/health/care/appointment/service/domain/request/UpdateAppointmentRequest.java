package com.health.care.appointment.service.domain.request;

import com.health.care.appointment.service.domain.enums.PatientGender;
import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateAppointmentRequest implements Serializable {
    private Long id;
    private String patientName;
    private Integer patientAge;
    private PatientGender patientGender;
    private String patientContactNo;
}

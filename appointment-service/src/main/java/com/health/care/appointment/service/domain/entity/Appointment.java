package com.health.care.appointment.service.domain.entity;

import com.health.care.appointment.service.domain.enums.PatientGender;
import com.health.care.appointment.service.domain.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "appointment")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Appointment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DOCTOR_ID", nullable = false)
    private String doctorId;

    @Column(name = "APPOINMENT_NO")
    private String appointmentNo;

    @Column(name = "APPOINTMENT_DATE", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "APPOINTMENT_TIME", nullable = false)
    private LocalTime appointmentTime;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "IS_PAYMENT_DONE" , nullable = false)
    private Boolean isPaymentDone;

    @Column(name = "PATIENT_NAME")
    private String patientName;

    @Column(name = "PATIENT_AGE")
    private Integer patientAge;

    @Column(name = "PATIENT_GENDER")
    @Enumerated(EnumType.STRING)
    private PatientGender patientGender;

    @Column(name = "PATIENT_ID" , nullable = false)
    private String patientId;

    @Column(name = "PATIENT_CONTACT_NO" , nullable = false)
    private String patientContactNo;

    @Column(name = "FEE")
    private BigDecimal fee;

    @Column(name = "APPROVED_BY")
    private String approvedBy;

    @Column(name = "APPROVED_TIME")
    private LocalDateTime approvedTime;

    @Column(name = "REASON", columnDefinition = "TEXT")
    private String reason;

}

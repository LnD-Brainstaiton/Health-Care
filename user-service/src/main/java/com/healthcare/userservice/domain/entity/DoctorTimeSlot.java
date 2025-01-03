package com.healthcare.userservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "DOCTOR_TIME_SLOT")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorTimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DOCTOR_ID")
    private String doctorId;

    @Column(name = "START_TIME")
    private LocalTime startTime;

    @Column(name = "END_TIME")
    private LocalTime endTime;

    @Column(name = "DAYS_OF_WEEK")
    private String daysOfWeek;

}

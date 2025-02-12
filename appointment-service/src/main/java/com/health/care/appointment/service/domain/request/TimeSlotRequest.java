package com.health.care.appointment.service.domain.request;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class TimeSlotRequest implements Serializable {
    private String doctorId;
    private LocalDate date;
}

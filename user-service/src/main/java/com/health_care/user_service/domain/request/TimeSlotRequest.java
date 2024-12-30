package com.health_care.user_service.domain.request;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class TimeSlotRequest implements Serializable {
    private String doctorId;
    private LocalDate date;
}

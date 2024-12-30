package com.health_care.user_service.domain.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TimeSlotResponse implements Serializable {
    private String doctorId;
    private List<String> timeSlotList;
}

package com.healthcare.userservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlotDto implements Serializable {
    private LocalTime startTime;
    private LocalTime endTime;
    private List<String> weekdays;
}

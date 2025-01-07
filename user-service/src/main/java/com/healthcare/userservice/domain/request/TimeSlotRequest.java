package com.healthcare.userservice.domain.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class TimeSlotRequest implements Serializable {
    private String doctorId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}

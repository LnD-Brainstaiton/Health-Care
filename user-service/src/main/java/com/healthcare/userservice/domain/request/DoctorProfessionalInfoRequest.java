package com.healthcare.userservice.domain.request;

import com.healthcare.userservice.domain.dto.TimeSlotDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class DoctorProfessionalInfoRequest implements Serializable {
    private int registrationNo;
    private String designation;
    private String department;
    private String specialities;
    List<TimeSlotDto> timeSlots;
    private String bloodGroup;
    private String dob;
    private double fee;
}

package com.healthcare.userservice.domain.request;

import com.healthcare.userservice.domain.dto.TimeSlotDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class DoctorProfessionalInfoRequest implements Serializable {
    @NotBlank(message = "userId cannot be blank")
    private String userId;

    @Min(value = 1, message = "Registration number must be greater than 0")
    private int registrationNo;

    @NotBlank(message = "Designation cannot be blank")
    @Size(max = 100, message = "Designation must not exceed 100 characters")
    private String designation;

    @NotBlank(message = "Department cannot be blank")
    @Size(max = 100, message = "Department must not exceed 100 characters")
    private String department;

    @NotBlank(message = "Specialities cannot be blank")
    @Size(max = 200, message = "Specialities must not exceed 200 characters")
    private String specialities;

    @NotEmpty(message = "Time slots cannot be empty")
    private List<@Valid TimeSlotDto> timeSlots;

    @Pattern(regexp = "^(A|B|AB|O)[+-]$", message = "Blood group must be valid (e.g., A+, O-, AB+)")
    private String bloodGroup;

    @NotBlank(message = "Date of birth cannot be blank")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date of birth must follow the format YYYY-MM-DD")
    private String dob;

    @Positive(message = "Fee must be greater than 0")
    private double fee;
}

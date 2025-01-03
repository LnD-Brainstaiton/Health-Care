package com.health_care.user_service.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorInfoResponse {
    private String doctorId;
    private String firstname;
    private String lastname;
    private String mobile;
    private String gender;
    private String email;
    private String designation;
    private String department;
    private String specialities;
    private double fee;
}

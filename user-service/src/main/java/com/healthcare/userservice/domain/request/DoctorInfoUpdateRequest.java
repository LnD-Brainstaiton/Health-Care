package com.healthcare.userservice.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorInfoUpdateRequest {
    private String firstname;
    private String lastname;
    private String mobile;
    private String gender;
    private String email;
    private String designation;
    private String department;
    private String specialities;
    private double fee;
    private String doctorId;
    private String password;
}

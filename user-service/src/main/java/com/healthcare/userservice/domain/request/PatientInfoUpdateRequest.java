package com.healthcare.userservice.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientInfoUpdateRequest {
    private String firstname;
    private String lastname;
    private String mobile;
    private String gender;
    private String address;
    private String email;
    private Integer age;
    private String nid;
    private String bloodGroup;
    private String patientId;
    private String password;
}

package com.health_care.user_service.domain.response;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientInfoResponse {

    private String firstname;

    private String lastname;

    private String mobile;

    private String gender; // Add validation at the service level for allowed values

    private String address;

     private String email;

    private Integer age;

    private String nid;

    private String bloodGroup;
}

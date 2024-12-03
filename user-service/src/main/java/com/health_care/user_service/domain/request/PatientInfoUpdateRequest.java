package com.health_care.user_service.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientInfoUpdateRequest {
    private String mobile;
    private String gender;
    private String address;
    private Integer age;
    private String nid;
    private String bloodGroup;
}

package com.healthcare.userservice.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorVacationRequest {
    private String startDate;
    private String endDate;
    private String reason;
}

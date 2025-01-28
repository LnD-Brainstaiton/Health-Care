package com.healthcare.userservice.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorDiscountRequest {
    private String discountDuration;
    private String discountRate;
}

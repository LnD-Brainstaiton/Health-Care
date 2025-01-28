package com.healthcare.userservice.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

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
    private Boolean isAvailable;
    private String unavailableDate;
    private BigDecimal rating;
    private List<RatingResponse> ratingResponseList;
}

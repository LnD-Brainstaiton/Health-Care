package com.healthcare.userservice.domain.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class RatingRequest {

    private BigDecimal rating;

    private String comment;

    private String userId;

    private String doctorId;

    private String commentParentId;

}

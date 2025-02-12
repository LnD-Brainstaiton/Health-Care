package com.healthcare.userservice.domain.dto;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class RatingReply {

    private String ratingId;

    private String comment;

    private String userId;

    private String doctorId;

    private String commentId;

    private String commentParentId;

    private LocalDateTime commentTime;
}

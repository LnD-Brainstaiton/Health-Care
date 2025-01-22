package com.healthcare.userservice.domain.response;

import com.healthcare.userservice.domain.dto.RatingReply;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class RatingResponse {

    private String ratingId;

    private BigDecimal rating;

    private String comment;

    private String userId;

    private String doctorId;

    private String commentId;

    private String commentParentId;

    private LocalDateTime commentTime;

    List<RatingReply> ratingReplyList;
}

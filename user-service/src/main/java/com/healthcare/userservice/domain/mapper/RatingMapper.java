package com.healthcare.userservice.domain.mapper;

import com.healthcare.userservice.domain.dto.RatingReply;
import com.healthcare.userservice.domain.entity.Rating;
import com.healthcare.userservice.domain.response.RatingResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RatingMapper {
    List<RatingResponse> toRatingResponse(List<Rating> rating);

    List<RatingReply> toRatingReply(List<Rating> rating);

    List<RatingReply> toRatingReplies(List<Rating> rating);
}

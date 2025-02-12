package com.healthcare.userservice.domain.mapper;

import com.health_care.id.generator.Impl.UniqueIdGeneratorImpl;
import com.healthcare.userservice.domain.dto.RatingReply;
import com.healthcare.userservice.domain.entity.Rating;
import com.healthcare.userservice.domain.request.RatingRequest;
import com.healthcare.userservice.domain.response.RatingResponse;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RatingMapper {
    RatingResponse mapToRatingResponse(Rating rating);

    // Map a list of Rating entities to RatingResponse
    default List<RatingResponse> mapToRatingResponse(List<Rating> ratingList) {
        return ratingList.stream()
                .filter(comment -> comment.getCommentParentId().equals("parent"))
                .map(parentComment -> {
                    RatingResponse ratingResponse = mapToRatingResponse(parentComment);
                    List<RatingReply> ratingReplyList = ratingList
                            .stream()
                            .filter(rating -> rating.getCommentParentId().equals(parentComment.getCommentId()))
                            .map(this::mapToRatingReply)
                            .toList();

                    ratingResponse.setRatingReplyList(ratingReplyList);
                    return ratingResponse;
                })
                .toList();
    }

    RatingReply mapToRatingReply(Rating rating);

    default Rating mapToRating(RatingRequest ratingRequest, UniqueIdGeneratorImpl uniqueIdGenerator,String commentPrefix,String ratingPrefix) {
        Rating rating = new Rating();

        rating.setDoctorId(ratingRequest.getDoctorId());
        rating.setRating(ratingRequest.getRating());
        rating.setComment(ratingRequest.getComment());
        rating.setUserId(ratingRequest.getUserId());
        rating.setCommentParentId(ratingRequest.getCommentParentId());
        rating.setCommentId(uniqueIdGenerator.generateUniqueIdWithPrefix(commentPrefix));
        rating.setRatingId(uniqueIdGenerator.generateUniqueIdWithPrefix(ratingPrefix));
        rating.setCommentTime(LocalDateTime.now());

        return rating;
    }

}

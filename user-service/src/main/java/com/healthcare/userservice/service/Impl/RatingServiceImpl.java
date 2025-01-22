package com.healthcare.userservice.service.Impl;

import com.health_care.id.generator.Impl.UniqueIdGeneratorImpl;
import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.entity.Rating;
import com.healthcare.userservice.domain.enums.ApiResponseCode;
import com.healthcare.userservice.domain.enums.ResponseMessage;
import com.healthcare.userservice.domain.mapper.RatingMapper;
import com.healthcare.userservice.domain.request.RatingRequest;
import com.healthcare.userservice.domain.response.RatingResponse;
import com.healthcare.userservice.repository.RatingRepository;
import com.healthcare.userservice.service.IRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements IRatingService {

    private final RatingRepository ratingRepository;
    private final UniqueIdGeneratorImpl uniqueIdGenerator;
    private final RatingMapper ratingMapper;

    @Value("${unique.id.comment.prefix}")
    private String commentPrefix;

    @Value("${unique.id.rating.prefix}")
    private String ratingPrefix;


    @Override
    public ApiResponse<Void> addRating(RatingRequest ratingRequest) {

        ratingRepository.save(ratingMapper.mapToRating(ratingRequest,uniqueIdGenerator,commentPrefix,ratingPrefix));

        return ApiResponse.<Void>builder()
                .responseCode(ApiResponseCode.OPERATION_SUCCESSFUL.getResponseCode())
                .responseMessage(ResponseMessage.OPERATION_SUCCESSFUL.getResponseMessage())
                .build();
    }
}

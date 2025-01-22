package com.healthcare.userservice.service;

import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.request.RatingRequest;
import com.healthcare.userservice.domain.response.RatingResponse;

import java.util.List;

public interface IRatingService {

    ApiResponse<Void> addRating(RatingRequest ratingRequest);
}

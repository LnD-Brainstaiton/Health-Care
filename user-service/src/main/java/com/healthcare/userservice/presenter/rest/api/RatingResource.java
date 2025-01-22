package com.healthcare.userservice.presenter.rest.api;

import com.healthcare.userservice.common.utils.AppUtils;
import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.request.RatingRequest;
import com.healthcare.userservice.domain.response.RatingResponse;
import com.healthcare.userservice.service.IRatingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AppUtils.BASE_URL)
@AllArgsConstructor
public class RatingResource {

    private final IRatingService ratingService;


    @PostMapping("/rating")
    public  ApiResponse<Void> addRating(@RequestBody RatingRequest ratingRequest) {
        return ratingService.addRating(ratingRequest);
    }


}

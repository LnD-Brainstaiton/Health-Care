package com.healthcare.userservice.repository;

import com.healthcare.userservice.domain.common.ApiResponse;
import com.healthcare.userservice.domain.entity.Rating;
import com.healthcare.userservice.domain.response.RatingResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findAllByDoctorId(String id);
}

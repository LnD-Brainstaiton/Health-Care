package com.healthcare.tfaservice.repository;


import com.healthcare.tfaservice.domain.entity.UserBadOtpCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBadOtpCountRepository extends JpaRepository<UserBadOtpCount, Long> {

    @Query("SELECT u FROM UserBadOtpCount u " +
            "WHERE u.userName = :userName")
    UserBadOtpCount findConfigurationByUser(String userName);

}

package com.healthcare.tfaservice.repository;


import com.healthcare.tfaservice.domain.entity.UserTfaBadOdtCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTfaBadOdtCountRepository extends JpaRepository<UserTfaBadOdtCount, Long> {

    @Query("SELECT u FROM UserTfaBadOdtCount u " +
            "WHERE u.userName = :userName")
    UserTfaBadOdtCount findConfigurationByUser(String userName);

}

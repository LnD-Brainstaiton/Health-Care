package com.healthcare.tfaservice.repository;

import com.healthcare.tfaservice.domain.entity.OneTimePassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OneTimePasswordRepository extends JpaRepository<OneTimePassword, Long> {

    OneTimePassword findBySessionIdAndUserName(String sessionId, String userName);

    OneTimePassword findByUserName(String userName);

}

package com.health_care.gateway.repository;

import com.health_care.gateway.domain.entity.PublicApi;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PublicApiRepository extends ReactiveCrudRepository<PublicApi, Long> {
    @Query("SELECT url FROM gateway.GATEWAY_PUBLIC_API WHERE is_active = true")
    Flux<String> findAllActiveUrls();
}

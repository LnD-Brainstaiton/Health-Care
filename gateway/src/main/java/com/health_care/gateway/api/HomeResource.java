package com.health_care.gateway.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HomeResource {

    @GetMapping("/")
    public Mono<String> home() {
        return Mono.just("Welcome to Health Care - Service Gateway is up and running.");
    }

}

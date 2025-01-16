package com.health_care.gateway.filter;

import com.health_care.gateway.repository.PublicApiRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class RouteValidator implements ApplicationRunner {

    private final PublicApiRepository apiRepository;
    private Set<String> openApiEndpoints = new HashSet<>();

    public RouteValidator(PublicApiRepository apiRepository) {
        this.apiRepository = apiRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        init()
                .doOnError(error -> System.err.println("Failed to initialize routes: " + error.getMessage()))
                .subscribe(); // Subscribe to trigger the reactive chain
    }

    public Mono<Void> init() {
        return apiRepository.findAllActiveUrls()
                .collect(Collectors.toSet())
                .doOnNext(endpoints -> this.openApiEndpoints = endpoints)
                .then();
    }

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
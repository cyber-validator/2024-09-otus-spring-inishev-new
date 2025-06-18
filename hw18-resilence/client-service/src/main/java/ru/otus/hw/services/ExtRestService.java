package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ExtRestService {

    private static final String SERVICE_NAME = "libService";

    private final RestClient restClient = RestClient.create();

    @CircuitBreaker(name = SERVICE_NAME, fallbackMethod = "circuitBreakerFallbackHandler")
    @Retry(name = SERVICE_NAME)
    @RateLimiter(name = SERVICE_NAME, fallbackMethod = "rateLimiterFallbackHandler")
    public String getBooks() {
        return restClient.get()
                .uri("http://localhost:8080/api/books")
                .retrieve()
                .body(String.class);
    }

    private String circuitBreakerFallbackHandler(Throwable ex) {
        return "Service unavailable, please try later";
    }

    private String rateLimiterFallbackHandler(Throwable ex) {
        return "Too many requests, try again later";
    }

}

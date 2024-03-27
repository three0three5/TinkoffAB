package org.example.app.client;

import io.swagger.client.model.RatesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.app.exception.NullBodyResponseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
@Slf4j
public class RatesClient {
    private final WebClient webClient;
    @Value("${rates-service.rates-path}")
    private final String ratesPath;

    @Retryable(maxAttemptsExpression  = "${rates-service.retry.maxAttempts}",
            backoff = @Backoff(
                    delayExpression = "${rates-service.retry.initDelay}",
                    multiplierExpression = "${rates-service.retry.multiplier}",
                    maxDelayExpression = "${rates-service.retry.maxDelay}"
            ))
    public Mono<RatesResponse> getRatesResponse() {
        return webClient.get()
                .uri(ratesPath)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(RatesResponse.class)
                .onErrorResume(error -> Mono.error(new NullBodyResponseException()));
    }
}

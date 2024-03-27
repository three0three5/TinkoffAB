package org.example.app.client;

import io.swagger.client.model.RatesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@RequiredArgsConstructor
@Component
@Slf4j
public class RatesClient {
    private final WebClient webClient;
    @Value("${rates-service.rates-path}")
    private final String ratesPath;

    public Mono<RatesResponse> getRatesResponse() {
        return webClient.get()
                .uri(ratesPath)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(RatesResponse.class)
                .retryWhen(Retry
                        .backoff(3, Duration.ofMillis(50))
                        .maxBackoff(Duration.ofMillis(150))
                        .doBeforeRetry(signal -> {
                            log.info("Retrying...");
                        }));
    }
}

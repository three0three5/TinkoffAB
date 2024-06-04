package org.example.app.client;

import io.swagger.client.model.RatesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.app.config.RatesClientProperties;
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
    private final WebClient ratesWebClient;
    private final RatesClientProperties properties;

    public Mono<RatesResponse> getRatesResponse() {
        return ratesWebClient.get()
                .uri(properties.getRatesPath())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(RatesResponse.class)
                .retryWhen(Retry
                        .backoff(properties.getMaxAttempts(), Duration.ofMillis(properties.getInitDelay()))
                        .maxBackoff(Duration.ofMillis(properties.getMaxDelay()))
                        .doBeforeRetry(signal -> log.info("Retrying...")));
    }
}

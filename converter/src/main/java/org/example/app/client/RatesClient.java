package org.example.app.client;

import io.swagger.client.model.RatesResponse;
import lombok.RequiredArgsConstructor;
import org.example.app.exception.NullBodyResponseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class RatesClient {
    private final WebClient webClient;
    @Value("${rates-service.rates-path}")
    private final String ratesPath;

    public RatesResponse getRatesResponse() {
        return webClient.get()
                .uri(ratesPath)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(RatesResponse.class)
                .onErrorResume(error -> Mono.error(new NullBodyResponseException()))
                .block();
    }
}

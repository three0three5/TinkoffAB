package org.example.accounts.client;

import io.swagger.client.model.Currency;
import lombok.RequiredArgsConstructor;
import org.example.accounts.dto.response.CurrencyResponseDto;
import org.example.accounts.exception.NullBodyResponseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Component
public class ConverterClient {
    private final WebClient webClient;
    @Value("${converter-service.converter-path}")
    private final String converterPath;

    public CurrencyResponseDto convertCurrency(Currency from, Currency to, BigDecimal amount) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(converterPath)
                .queryParam("from", from)
                .queryParam("to", to)
                .queryParam("amount", amount);
        return webClient.get()
                .uri(builder.build().toUri())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(CurrencyResponseDto.class)
                .onErrorResume(error -> Mono.error(new NullBodyResponseException()))
                .block();
    }
}

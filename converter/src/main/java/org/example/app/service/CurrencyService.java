package org.example.app.service;

import io.swagger.client.model.Currency;
import io.swagger.client.model.RatesResponse;
import lombok.RequiredArgsConstructor;
import org.example.app.dto.CurrencyResponseDto;
import org.example.app.exception.AmountNotPositiveException;
import org.example.app.exception.CurrencyNotAvailableException;
import org.example.app.exception.NullBodyResponseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final WebClient webClient;

    @Value("${rates-service.rates-path}")
    private final String ratesPath;

    public CurrencyResponseDto convert(Currency from, Currency to, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new AmountNotPositiveException();

        RatesResponse rates = getRatesResponse();

        BigDecimal amountConverted = convertRate(rates, from, to, amount);
        return new CurrencyResponseDto(to, amountConverted);
    }

    private RatesResponse getRatesResponse() {
        return webClient.get()
                .uri(ratesPath)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(RatesResponse.class)
                .onErrorResume(error -> Mono.error(new NullBodyResponseException()))
                .block();
    }

    private BigDecimal convertRate(RatesResponse rates, Currency from, Currency to, BigDecimal toConvert) {
        Map<String, BigDecimal> m = rates.getRates();
        BigDecimal baseToFirst = m.get(from.name());
        BigDecimal baseToSecond = m.get(to.name());
        if (baseToFirst == null) throw new CurrencyNotAvailableException(from.name());
        if (baseToSecond == null) throw new CurrencyNotAvailableException(to.name());
        return baseToFirst.multiply(toConvert).divide(baseToSecond, 2, RoundingMode.HALF_EVEN);
    }
}

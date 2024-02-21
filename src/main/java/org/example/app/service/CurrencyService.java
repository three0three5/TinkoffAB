package org.example.app.service;

import io.swagger.client.model.Currency;
import io.swagger.client.model.RatesResponse;
import lombok.RequiredArgsConstructor;
import org.example.app.dto.CurrencyResponseDto;
import org.example.app.exception.AmountNotPositiveException;
import org.example.app.exception.CurrencyNotAvailableException;
import org.example.app.exception.NullBodyResponseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final RestTemplate restTemplate;

    @Value("${rates-service.name}")
    private String ratesName;

    @Value("${rates-service.port}")
    private int ratesPort;

    public CurrencyResponseDto convert(String from, String to, double amount) {
        if (amount <= 0) throw new AmountNotPositiveException();
        Currency[] currencies = getCurrencies(new String[]{from, to});

        RatesResponse rates = getRatesResponse();

        BigDecimal amountConverted = convertRate(rates, currencies[0], currencies[1], amount);
        return new CurrencyResponseDto(currencies[1], amountConverted);
    }

    private RatesResponse getRatesResponse() {
        String url = "http://" + ratesName + ":" + ratesPort;
        ResponseEntity<RatesResponse> response = restTemplate
                .getForEntity(url + "/rates", RatesResponse.class);
        RatesResponse rates = response.getBody();
        if (rates == null) throw new NullBodyResponseException();
        return rates;
    }

    private BigDecimal convertRate(RatesResponse rates, Currency from, Currency to, double toConvert) {
        Map<String, BigDecimal> m = rates.getRates();
        BigDecimal baseToFirst = m.get(from.toString());
        BigDecimal baseToSecond = m.get(to.toString());
        double multiplier = baseToFirst.doubleValue() / baseToSecond.doubleValue();
        multiplier *= toConvert;
        return BigDecimal.valueOf(multiplier)
                .setScale(10, RoundingMode.HALF_EVEN) // без этого иногда падает
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    private Currency[] getCurrencies(String[] strings) {
        int n = strings.length;
        if (n == 0) return new Currency[]{};
        Currency[] result = new Currency[n];
        for (int i = 0; i < n; ++i) {
            result[i] = Currency.fromValue(strings[i]);
            if (result[i] == null)
                throw new CurrencyNotAvailableException(strings[i]);
        }
        return result;
    }
}

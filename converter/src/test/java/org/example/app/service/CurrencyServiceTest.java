package org.example.app.service;

import io.swagger.client.model.Currency;
import io.swagger.client.model.RatesResponse;
import org.example.app.client.RatesClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {
    @InjectMocks
    private CurrencyService currencyService;
    @Mock
    private RatesClient client;

    @BeforeEach
    void setUp() {
        Map<String, BigDecimal> m = new HashMap<>();
        m.put("RUB", BigDecimal.ONE);
        m.put("EUR", BigDecimal.valueOf(100));
        m.put("CNY", BigDecimal.valueOf(1335).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN));
        RatesResponse response = new RatesResponse().base(Currency.RUB).rates(m);
        when(client.getRatesResponse()).thenReturn(response);
    }

    @Test
    void givenRublesToEuros_whenConvert_thenGetEuros() {
        var response = currencyService.convert(Currency.RUB, Currency.EUR, BigDecimal.valueOf(200));
        assertEquals(BigDecimal.valueOf(2).setScale(2, RoundingMode.HALF_EVEN), response.amount());
    }

    @Test
    void givenEurosToRubles_whenConvert_thenGetRubles() {
        var response = currencyService.convert(Currency.EUR, Currency.RUB, BigDecimal.valueOf(3.5));
        assertEquals(BigDecimal.valueOf(350).setScale(2, RoundingMode.HALF_EVEN), response.amount());
    }

    @Test
    void givenTrickyDoubleValue_whenConvert_thenHalfEvenResult() {
        var response = currencyService.convert(Currency.CNY, Currency.RUB, BigDecimal.valueOf(944.5));
        assertEquals(BigDecimal.valueOf(12609.08).setScale(2, RoundingMode.HALF_EVEN), response.amount());
    }
}
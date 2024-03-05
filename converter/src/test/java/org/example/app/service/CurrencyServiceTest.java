package org.example.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.client.model.Currency;
import io.swagger.client.model.RatesResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {
    private static String ratesPath;
    private CurrencyService currencyService;

    private WebClient webClient = WebClient.builder().build();

    private static MockWebServer mockBackEnd;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void init() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void setUp() throws JsonProcessingException {
        Map<String, BigDecimal> m = new HashMap<>();
        m.put("RUB", BigDecimal.ONE);
        m.put("EUR", BigDecimal.valueOf(100));
        m.put("CNY", BigDecimal.valueOf(1335).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN));
        RatesResponse response = new RatesResponse().base(Currency.RUB).rates(m);
        mockBackEnd.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(response))
                .addHeader("Content-Type", "application/json"));
        ratesPath = String.format("http://localhost:%s", mockBackEnd.getPort());
        currencyService = new CurrencyService(webClient, ratesPath);
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
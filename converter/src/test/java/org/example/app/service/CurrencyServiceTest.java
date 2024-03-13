package org.example.app.service;

import com.example.grpc.CurrencyProto;
import com.example.grpc.CurrencyRequest;
import io.swagger.client.model.Currency;
import io.swagger.client.model.RatesResponse;
import org.example.app.client.RatesClient;
import org.example.app.mapper.ProtoMapper;
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
    private CurrencyService currencyService;
    @Mock
    private RatesClient client;
    private ProtoMapper mapper = new ProtoMapper();

    @BeforeEach
    void setUp() {
        Map<String, BigDecimal> m = new HashMap<>();
        m.put("RUB", BigDecimal.ONE);
        m.put("EUR", BigDecimal.valueOf(100));
        m.put("CNY", BigDecimal.valueOf(1335).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN));
        RatesResponse response = new RatesResponse().base(Currency.RUB).rates(m);
        when(client.getRatesResponse()).thenReturn(response);
        currencyService = new CurrencyService(client, mapper);
    }

    @Test
    void givenRublesToEuros_whenConvert_thenGetEuros() {
        CurrencyRequest request = CurrencyRequest.newBuilder()
                .setFrom(CurrencyProto.RUB)
                .setTo(CurrencyProto.EUR)
                .setAmount(mapper.mapBigDecimalToDecimalValue(BigDecimal.valueOf(200)))
                .build();
        var response = currencyService.convert(request);
        assertEquals(BigDecimal.valueOf(2).setScale(2, RoundingMode.HALF_EVEN),
                mapper.mapDecimalValueToBigDecimal(response.getConvertedAmount()));
    }

    @Test
    void givenEurosToRubles_whenConvert_thenGetRubles() {
        CurrencyRequest request = CurrencyRequest.newBuilder()
                .setFrom(CurrencyProto.EUR)
                .setTo(CurrencyProto.RUB)
                .setAmount(mapper.mapBigDecimalToDecimalValue(BigDecimal.valueOf(3.5)))
                .build();
        var response = currencyService.convert(request);
        assertEquals(BigDecimal.valueOf(350).setScale(2, RoundingMode.HALF_EVEN),
                mapper.mapDecimalValueToBigDecimal(response.getConvertedAmount()));
    }

    @Test
    void givenTrickyDoubleValue_whenConvert_thenHalfEvenResult() {
        CurrencyRequest request = CurrencyRequest.newBuilder()
                .setFrom(CurrencyProto.CNY)
                .setTo(CurrencyProto.RUB)
                .setAmount(mapper.mapBigDecimalToDecimalValue(BigDecimal.valueOf(944.5)))
                .build();
        var response = currencyService.convert(request);
        assertEquals(BigDecimal.valueOf(12609.08).setScale(2, RoundingMode.HALF_EVEN),
                mapper.mapDecimalValueToBigDecimal(response.getConvertedAmount()));
    }
}
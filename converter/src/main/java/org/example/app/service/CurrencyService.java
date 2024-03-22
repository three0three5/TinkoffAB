package org.example.app.service;

import com.example.grpc.CurrencyRequest;
import com.example.grpc.CurrencyResponse;
import io.swagger.client.model.Currency;
import io.swagger.client.model.RatesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.app.client.RatesClient;
import org.example.app.exception.AmountNotPositiveException;
import org.example.app.exception.CurrencyNotAvailableException;
import org.example.app.mapper.ProtoMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyService {
    private final RatesClient ratesClient;
    private final ProtoMapper mapper;

    public Mono<CurrencyResponse> convert(CurrencyRequest request) {
        BigDecimal amount = mapper.mapDecimalValueToBigDecimal(request.getAmount());
        Currency from = mapper.mapCurrencyProtoToCurrency(request.getFrom());
        Currency to = mapper.mapCurrencyProtoToCurrency(request.getTo());

        if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new AmountNotPositiveException();

        Mono<RatesResponse> rates = ratesClient.getRatesResponse();
        return rates
                .map(ratesResponse -> convertRate(ratesResponse, from, to, amount))
                .map(bigDecimal -> CurrencyResponse.newBuilder()
                        .setBase(request.getTo())
                        .setConvertedAmount(mapper.mapBigDecimalToDecimalValue(bigDecimal))
                        .build());
    }

    private BigDecimal convertRate(RatesResponse rates, Currency from, Currency to, BigDecimal toConvert) {
        log.info("to convert: " + toConvert);
        Map<String, BigDecimal> m = rates.getRates();
        BigDecimal baseToFirst = m.get(from.name());
        BigDecimal baseToSecond = m.get(to.name());
        if (baseToFirst == null) throw new CurrencyNotAvailableException(from.name());
        if (baseToSecond == null) throw new CurrencyNotAvailableException(to.name());
        return baseToFirst.multiply(toConvert).divide(baseToSecond, 2, RoundingMode.HALF_EVEN);
    }
}

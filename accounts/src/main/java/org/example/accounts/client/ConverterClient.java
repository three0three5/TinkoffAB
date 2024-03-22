package org.example.accounts.client;

import com.example.grpc.ConverterServiceGrpc;
import com.example.grpc.CurrencyRequest;
import com.example.grpc.CurrencyResponse;
import io.swagger.client.model.Currency;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.accounts.dto.response.CurrencyResponseDto;
import org.example.accounts.mapper.ProtoMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class ConverterClient {
    private final ProtoMapper mapper;
    @GrpcClient("converterClient")
    private ConverterServiceGrpc.ConverterServiceBlockingStub converterService;

    public CurrencyResponseDto convertCurrency(Currency from, Currency to, BigDecimal amount) {
        CurrencyRequest request = CurrencyRequest.newBuilder()
                .setFrom(mapper.mapCurrencyToCurrencyProto(from))
                .setTo(mapper.mapCurrencyToCurrencyProto(to))
                .setAmount(mapper.mapBigDecimalToDecimalValue(amount))
                .build();
        CurrencyResponse response = converterService.getConvertedCurrency(request);
        return new CurrencyResponseDto(
                mapper.mapCurrencyProtoToCurrency(response.getBase()),
                mapper.mapDecimalValueToBigDecimal(response.getConvertedAmount())
        );
    }
}

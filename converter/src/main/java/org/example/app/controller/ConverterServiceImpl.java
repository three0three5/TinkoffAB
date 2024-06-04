package org.example.app.controller;

import com.example.grpc.CurrencyRequest;
import com.example.grpc.CurrencyResponse;
import com.example.grpc.ReactorConverterServiceGrpc;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.app.service.CurrencyService;
import reactor.core.publisher.Mono;

@GrpcService
@RequiredArgsConstructor
@Slf4j
@Observed(name = "ConverterServiceImpl")
public class ConverterServiceImpl
        extends ReactorConverterServiceGrpc.ConverterServiceImplBase {
    private final CurrencyService currencyService;

    public Mono<CurrencyResponse> getConvertedCurrency(Mono<CurrencyRequest> requestMono) {
        return requestMono
                .doOnNext(request -> log.info(request.getFrom() + " " +
                        request.getTo() + " " + request.getAmount()))
                .flatMap(currencyService::convert);
    }
}

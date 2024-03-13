package org.example.app.controller;

import com.example.grpc.ConverterServiceGrpc;
import com.example.grpc.CurrencyRequest;
import com.example.grpc.CurrencyResponse;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.app.service.CurrencyService;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class ConverterServiceImpl
        extends ConverterServiceGrpc.ConverterServiceImplBase {
    private final CurrencyService currencyService;

    public void getConvertedCurrency(
            CurrencyRequest request,
            StreamObserver<CurrencyResponse> responseObserver) {
        log.info(request.getFrom() + " " + request.getTo() + " " +
                request.getAmount());
        CurrencyResponse response = currencyService.convert(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}

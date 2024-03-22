package org.example.app.controller;

import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import org.example.app.exception.AmountNotPositiveException;
import org.example.app.exception.CurrencyNotAvailableException;
import org.example.app.exception.NullBodyResponseException;

@GrpcAdvice
public class GrpcExceptionAdvice {
    @GrpcExceptionHandler
    public Status handleInvalidCurrencyMapping(CurrencyNotAvailableException e) {
        return Status.INVALID_ARGUMENT.withDescription(e.getMessage())
                .withCause(e);
    }

    @GrpcExceptionHandler
    public Status handleAmountNotPositive(AmountNotPositiveException e) {
        return Status.INVALID_ARGUMENT.withDescription(e.getMessage())
                .withCause(e);
    }

    @GrpcExceptionHandler
    public Status handleNullBodyResponse(NullBodyResponseException e) {
        return Status.UNAVAILABLE.withCause(e);
    }
}

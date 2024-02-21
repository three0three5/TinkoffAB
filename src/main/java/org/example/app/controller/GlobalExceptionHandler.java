package org.example.app.controller;

import org.example.app.dto.ErrorResponseDto;
import org.example.app.exception.CurrencyException;
import org.example.app.exception.NullBodyResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleNullBodyResponse(NullBodyResponseException e) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(
                new ErrorResponseDto(e.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleCurrency(CurrencyException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponseDto(e.getMessage())
        );
    }
}

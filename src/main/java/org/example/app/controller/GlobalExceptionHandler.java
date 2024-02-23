package org.example.app.controller;

import lombok.RequiredArgsConstructor;
import org.example.app.dto.ErrorResponseDto;
import org.example.app.exception.CurrencyException;
import org.example.app.exception.NullBodyResponseException;
import org.example.app.exception.converter.MethodArgumentTypeMismatchConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final MethodArgumentTypeMismatchConverter mismatchConverter;

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleNullBodyResponse(NullBodyResponseException e) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(
                new ErrorResponseDto(e.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleCurrencyException(CurrencyException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponseDto(e.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleArgumentMismatch(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponseDto(mismatchConverter.convertToMessage(e))
        );
    }
}

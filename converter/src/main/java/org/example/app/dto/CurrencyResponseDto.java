package org.example.app.dto;

import io.swagger.client.model.Currency;

import java.math.BigDecimal;

public record CurrencyResponseDto(Currency currency, BigDecimal amount) {
}

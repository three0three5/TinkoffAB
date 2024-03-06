package org.example.accounts.dto.response;

import io.swagger.client.model.Currency;

import java.math.BigDecimal;

public record CurrencyResponseDto(Currency currency, BigDecimal amount) {
}

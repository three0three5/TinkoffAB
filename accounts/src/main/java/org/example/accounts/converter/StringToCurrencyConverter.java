package org.example.accounts.converter;

import io.swagger.client.model.Currency;
import org.example.accounts.exception.InvalidCurrencyParamException;
import org.springframework.core.convert.converter.Converter;

import static org.example.accounts.utils.Constants.INVALID_CURRENCY_PARAM_MESSAGE;

public class StringToCurrencyConverter implements Converter<String, Currency> {
    @Override
    public Currency convert(String source) {
        try {
            return Currency.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCurrencyParamException(INVALID_CURRENCY_PARAM_MESSAGE);
        }
    }
}

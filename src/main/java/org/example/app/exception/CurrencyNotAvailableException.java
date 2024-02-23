package org.example.app.exception;

import static org.example.app.utils.Constants.CURRENCY_NOT_AVAILABLE;

public class CurrencyNotAvailableException extends CurrencyException {
    public CurrencyNotAvailableException(String message) {
        super(CURRENCY_NOT_AVAILABLE.formatted(message));
    }
}

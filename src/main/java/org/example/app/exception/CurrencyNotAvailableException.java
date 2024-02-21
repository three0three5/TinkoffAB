package org.example.app.exception;

public class CurrencyNotAvailableException extends CurrencyException {
    public CurrencyNotAvailableException(String message) {
        super("Валюта " + message + " недоступна");
    }
}

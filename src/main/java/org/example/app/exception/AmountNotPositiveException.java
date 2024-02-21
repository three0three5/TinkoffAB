package org.example.app.exception;

public class AmountNotPositiveException extends CurrencyException {
    public AmountNotPositiveException(String message) {
        super(message);
    }

    public AmountNotPositiveException() {
        super("Отрицательная сумма");
    }
}

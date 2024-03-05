package org.example.app.exception;

import static org.example.app.utils.Constants.AMOUNT_NOT_POSITIVE;

public class AmountNotPositiveException extends CurrencyException {
    public AmountNotPositiveException(String message) {
        super(message);
    }

    public AmountNotPositiveException() {
        super(AMOUNT_NOT_POSITIVE);
    }
}

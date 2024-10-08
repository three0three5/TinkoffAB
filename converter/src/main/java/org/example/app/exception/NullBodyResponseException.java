package org.example.app.exception;

import static org.example.app.utils.Constants.NULL_BODY_EXCEPTION;

public class NullBodyResponseException extends RuntimeException {
    public NullBodyResponseException(String message) {
        super(message);
    }

    public NullBodyResponseException() {
        super(NULL_BODY_EXCEPTION);
    }
}

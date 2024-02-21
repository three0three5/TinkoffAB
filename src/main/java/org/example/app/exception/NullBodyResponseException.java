package org.example.app.exception;

public class NullBodyResponseException extends RuntimeException {
    public NullBodyResponseException(String message) {
        super(message);
    }

    public NullBodyResponseException() {
        super("Service is unavailable");
    }
}

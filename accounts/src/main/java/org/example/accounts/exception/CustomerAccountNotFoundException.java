package org.example.accounts.exception;

import jakarta.persistence.EntityNotFoundException;

import static org.example.accounts.utils.Constants.CUSTOMER_ACCOUNT_NOT_FOUND;

public class CustomerAccountNotFoundException extends EntityNotFoundException {
    public CustomerAccountNotFoundException(String message) {
        super(message);
    }

    public CustomerAccountNotFoundException() {
        super(CUSTOMER_ACCOUNT_NOT_FOUND);
    }
}

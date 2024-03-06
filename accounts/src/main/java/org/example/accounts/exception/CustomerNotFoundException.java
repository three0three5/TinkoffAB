package org.example.accounts.exception;


import jakarta.persistence.EntityNotFoundException;

import static org.example.accounts.utils.Constants.CUSTOMER_NOT_FOUND;

public class CustomerNotFoundException extends EntityNotFoundException {
    public CustomerNotFoundException(String message) {
        super(message);
    }

    public CustomerNotFoundException() {
        super(CUSTOMER_NOT_FOUND);
    }
}

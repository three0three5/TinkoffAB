package com.example.idempotency.utils;

public record IdempotencyKey(String keyValue) {
    @Override
    public String toString() {
        return "IdempotencyKey:%s".formatted(keyValue);
    }
}

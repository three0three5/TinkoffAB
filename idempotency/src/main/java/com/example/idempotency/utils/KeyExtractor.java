package com.example.idempotency.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public interface KeyExtractor {
    Optional<IdempotencyKey> extractKey(HttpServletRequest request);
    void setHeaderName(String name);
}

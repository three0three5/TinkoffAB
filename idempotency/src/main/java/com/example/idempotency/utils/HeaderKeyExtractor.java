package com.example.idempotency.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@RequiredArgsConstructor
@Setter
public class HeaderKeyExtractor implements KeyExtractor {
    private String headerName;

    @Override
    public Optional<IdempotencyKey> extractKey(HttpServletRequest request) {
        String headerKey = request.getHeader(headerName);
        if (headerKey == null) return Optional.empty();
        return Optional.of(new IdempotencyKey(headerKey));
    }
}

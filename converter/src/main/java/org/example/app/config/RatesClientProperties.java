package org.example.app.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter
public class RatesClientProperties {
    @Value("${rates-service.rates-path}")
    private final String ratesPath;

    @Value("${rates-service.url}")
    private final String ratesUrl;

    @Value("${rates-service.retry.maxAttempts}")
    private final int maxAttempts;

    @Value("${rates-service.retry.maxDelay}")
    private final int maxDelay;

    @Value("${rates-service.retry.initDelay}")
    private final int initDelay;
}

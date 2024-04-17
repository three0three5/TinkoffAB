package com.example.idempotency.utils;

import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

public record IdempotencyValue(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, boolean isDone) {
}

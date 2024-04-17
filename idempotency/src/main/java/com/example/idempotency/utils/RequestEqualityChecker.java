package com.example.idempotency.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.util.ContentCachingRequestWrapper;

public interface RequestEqualityChecker {
    boolean equals(HttpServletRequest incomingRequest, ContentCachingRequestWrapper persistedRequest);
}

package com.example.idempotency.utils;

import com.example.idempotency.serializer.RequestSerializable;
import jakarta.servlet.http.HttpServletRequest;

public interface RequestEqualityChecker {
    boolean equals(HttpServletRequest incomingRequest, RequestSerializable persistedRequest);
}

package com.example.idempotency.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

@Slf4j
public class DefaultRequestEqualityChecker implements RequestEqualityChecker {
    @Override
    public boolean equals(HttpServletRequest incomingRequest, ContentCachingRequestWrapper persistedRequest) {
        boolean requestsBodiesEqual;
        ContentCachingRequestWrapper requestWrapped = new ContentCachingRequestWrapper(incomingRequest);
        try {
            requestWrapped.getReader().read();
            String body1 = getRequestBody(requestWrapped);
            String body2 = getRequestBody(persistedRequest);
            requestsBodiesEqual = body1.equals(body2);
        } catch (IOException e) {
            log.warn("Exception while checking request body equality: %s".formatted(e.getMessage()));
            throw new RuntimeException(e);
        }
        return requestWrapped.getRequestURI().equals(persistedRequest.getRequestURI()) &&
                requestWrapped.getMethod().equals(persistedRequest.getMethod()) &&
                requestsBodiesEqual;
    }

    private String getRequestBody(ContentCachingRequestWrapper request) throws IOException {
        try {
            return new String(request.getContentAsByteArray(), request.getCharacterEncoding());
        } catch (IOException e) {
            log.error("Error reading request body", e);
            return "";
        }
    }
}

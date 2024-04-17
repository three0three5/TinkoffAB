package com.example.idempotency.serializer;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class RequestSerializable {
    private String method;
    private String uri;
    private byte[] content;
    private String contentType;
    private Map<String, String> headers = new HashMap<>();
    private String encoding;

    public RequestSerializable(ContentCachingRequestWrapper requestWrapper) {
        encoding = requestWrapper.getCharacterEncoding();
        content = requestWrapper.getContentAsByteArray();
        method = requestWrapper.getMethod();
        uri = requestWrapper.getRequestURI();
        requestWrapper.getHeaderNames().asIterator().forEachRemaining(name -> {
            String value = String.join(", ", Collections.list(requestWrapper.getHeaders(name)));
            headers.put(name, value);
        });
        contentType = requestWrapper.getContentType();
    }
}

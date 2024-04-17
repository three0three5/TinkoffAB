package com.example.idempotency.serializer;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
@NoArgsConstructor
public class ResponseSerializable implements Serializable {
    private byte[] content;
    private String contentType;
    private Map<String, Collection<String>> headers = new HashMap<>();
    private int status;
    private int contentLength;
    private String encoding;

    public ResponseSerializable(ContentCachingResponseWrapper response) {
        encoding = response.getCharacterEncoding();
        content = response.getContentAsByteArray();
        status = response.getStatus();
        response.getHeaderNames().forEach(name -> {
            headers.put(name, response.getHeaders(name));
        });
        contentType = response.getContentType();
        contentLength = content.length;
    }

    public void copyToHttpServletResponse(HttpServletResponse response) {
        response.setStatus(status);
        headers.forEach((headerName, toAdd) -> {
            toAdd.forEach(value -> response.setHeader(headerName, value));
        });
        response.setContentLength(contentLength);
        response.setContentType(contentType);
        try {
            ServletOutputStream stream = response.getOutputStream();
            stream.write(content);
            stream.flush();
        } catch (IOException e) {
            log.error("error while writing in response body");
            throw new RuntimeException(e);
        }
    }
}

package com.example.idempotency.utils;

import com.example.idempotency.IdempotencyProperties;
import com.example.idempotency.Idempotent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.InvocationTargetException;

@RequiredArgsConstructor
@Slf4j
public class HandlerMethodUtils {
    private final IdempotencyProperties properties;

    public long getLockExpirationTime(HandlerMethod handlerMethod) {
        long val = handlerMethod.getMethodAnnotation(Idempotent.class).lockExpireTimeSeconds();
        if (val == -1) return properties.getLockExpireTime();
        return val;
    }

    public KeyExtractor getExtractor(HandlerMethod handlerMethod) {
        Class<? extends KeyExtractor> clazz = handlerMethod.getMethodAnnotation(Idempotent.class).keyExtractor();
        try {
            KeyExtractor instance = clazz.getConstructor().newInstance();
            String headerName = handlerMethod.getMethodAnnotation(Idempotent.class).headerName();
            if (headerName.length() == 0) {
                headerName = properties.getHeaderKey();
            }
            instance.setHeaderName(headerName);
            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public RequestEqualityChecker getChecker(HandlerMethod handlerMethod) {
        Class<? extends RequestEqualityChecker> clazz =
                handlerMethod.getMethodAnnotation(Idempotent.class).requestEqualityChecker();
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

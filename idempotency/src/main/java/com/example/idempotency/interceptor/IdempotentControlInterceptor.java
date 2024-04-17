package com.example.idempotency.interceptor;

import com.example.idempotency.Idempotent;
import com.example.idempotency.client.RedisIdempotencyClient;
import com.example.idempotency.serializer.RequestSerializable;
import com.example.idempotency.serializer.ResponseSerializable;
import com.example.idempotency.utils.HandlerMethodUtils;
import com.example.idempotency.utils.IdempotencyKey;
import com.example.idempotency.utils.IdempotencyValue;
import com.example.idempotency.utils.KeyExtractor;
import com.example.idempotency.utils.RequestEqualityChecker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class IdempotentControlInterceptor implements HandlerInterceptor {
    private final RedisIdempotencyClient redisClient;
    private final HandlerMethodUtils methodUtils;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) return true;
        if (!handlerMethod.hasMethodAnnotation(Idempotent.class)) return true;

        KeyExtractor extractor = methodUtils.getExtractor(handlerMethod);
        Optional<IdempotencyKey> headerKey = extractor.extractKey(request);
        if (headerKey.isEmpty()) return true;
        log.info("Key: {}", headerKey);

        long lockExpirationTime = methodUtils.getLockExpirationTime(handlerMethod);
        long ttlSeconds = methodUtils.getTimeToLive(handlerMethod);
        IdempotencyValue value = new IdempotencyValue(null, null, false);
        boolean isAbsent = redisClient.setIfNotExists(headerKey.get(), value, lockExpirationTime, ttlSeconds);

        if (isAbsent) {
            log.info("lock acquired for key {}\n", headerKey.get());
            return true;
        }
        log.info("lock exists already for key {}\n", headerKey.get());

        value = redisClient.get(headerKey.get());
        if (!value.isDone()) {
            log.info("request is in process");
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return false;
        }
        
        log.info("request is done; check requests equality");
        RequestEqualityChecker checker = methodUtils.getChecker(handlerMethod);
        if (!checker.equals(request, value.request())) {
            log.info("requests are not equal");
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } else {
            log.info("requests are equal");
            value.response().copyToHttpServletResponse(response);
        }
        return false;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler,
                                @Nullable Exception ex) {
        if (!(handler instanceof HandlerMethod handlerMethod)) return;
        if (!handlerMethod.hasMethodAnnotation(Idempotent.class)) return;

        log.info("after completion exception is null: {}\n", ex == null);
        KeyExtractor extractor = methodUtils.getExtractor(handlerMethod);
        Optional<IdempotencyKey> headerKey = extractor.extractKey(request);
        if (headerKey.isEmpty()) return;
        log.info("Key: {}", headerKey);

        RequestSerializable requestRedis = new RequestSerializable((ContentCachingRequestWrapper)request);
        ResponseSerializable responseRedis = new ResponseSerializable((ContentCachingResponseWrapper)response);
        IdempotencyValue value = new IdempotencyValue(requestRedis, responseRedis, true);
        long ttlSeconds = methodUtils.getTimeToLive(handlerMethod);
        redisClient.set(headerKey.get(), value, ttlSeconds);
    }
}

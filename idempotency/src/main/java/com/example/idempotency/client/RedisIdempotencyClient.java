package com.example.idempotency.client;

import com.example.idempotency.utils.IdempotencyKey;
import com.example.idempotency.utils.IdempotencyValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
public class RedisIdempotencyClient {
    private final RedisTemplate<String, IdempotencyValue> redisTemplate;

    public boolean setIfNotExists(IdempotencyKey key, IdempotencyValue value, long lockExpirationTime, long ttlInSeconds) {
        Boolean success = redisTemplate.opsForValue().setIfAbsent(
                key.toString(), value, lockExpirationTime, TimeUnit.SECONDS);
        if (success != null && success) {
            redisTemplate.expire(key.toString(), ttlInSeconds, TimeUnit.SECONDS);
        }
        return success != null && success;
    }

    public IdempotencyValue get(IdempotencyKey key) {
        return redisTemplate.opsForValue().get(key.toString());
    }

    public void set(IdempotencyKey key, IdempotencyValue value, long ttlInSeconds) {
        redisTemplate.opsForValue().set(key.toString(), value);
        redisTemplate.expire(key.toString(), ttlInSeconds, TimeUnit.SECONDS);
    }
}

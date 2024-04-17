package com.example.idempotency.client;

import com.example.idempotency.utils.IdempotencyKey;
import com.example.idempotency.utils.IdempotencyValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

@RequiredArgsConstructor
@Slf4j
public class RedisIdempotencyClient {
    private final RedisTemplate<String, IdempotencyValue> redisTemplate;

    public boolean setIfNotExists(IdempotencyKey key, IdempotencyValue value, long lockExpirationTime) {
        Boolean success = redisTemplate.opsForValue().setIfAbsent(key.toString(), value);
        return success != null && success;
    }

    public IdempotencyValue get(IdempotencyKey key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void set(IdempotencyKey key, IdempotencyValue value) {
        redisTemplate.opsForValue().set(key.toString(), value);
    }
}

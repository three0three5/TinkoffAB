package com.example.idempotency;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.example.idempotency.utils.DefaultConstants.HEADER_NAME;
import static com.example.idempotency.utils.DefaultConstants.LOCK_EXPIRED_SECONDS;
import static com.example.idempotency.utils.DefaultConstants.TIME_TO_LIVE_SECONDS;

@ConfigurationProperties(prefix = "idempotency")
@Setter
@Getter
public class IdempotencyProperties {
    /**
     * Header where idempotence key is placed
     */
    private String headerKey = HEADER_NAME;
    /**
     * Time to live for cached result in seconds
     */
    private long timeToLiveSeconds = TIME_TO_LIVE_SECONDS;
    /**
     * Time for lock to be expired in seconds
     */
    private long lockExpireTime = LOCK_EXPIRED_SECONDS;
    /**
     * Whether idempotency is enabled or not
     */
    private boolean enabled = true;
    /**
     * Redis params
     */
    private String redisHost;
    /**
     * Redis params
     */
    private int redisPort;
}

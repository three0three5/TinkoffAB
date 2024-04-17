package com.example.idempotency.autoconfigure;

import com.example.idempotency.IdempotencyProperties;
import com.example.idempotency.client.RedisIdempotencyClient;
import com.example.idempotency.filter.ContentCachingFilter;
import com.example.idempotency.interceptor.IdempotentControlInterceptor;
import com.example.idempotency.utils.HandlerMethodUtils;
import com.example.idempotency.utils.IdempotencyKey;
import com.example.idempotency.utils.IdempotencyValue;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@ConditionalOnProperty(
        value = "idempotency.enabled",
        havingValue = "true",
        matchIfMissing = true
)
@EnableConfigurationProperties(IdempotencyProperties.class)
public class IdempotencyAutoConfiguration {

    @Bean
    public WebMvcConfigurer idempotentWebMvcConfigurer(HandlerInterceptor idempotencyInterceptor) {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(@NonNull InterceptorRegistry registry) {
                var registration = registry.addInterceptor(idempotencyInterceptor);
                registration.addPathPatterns();
            }
        };
    }

    @Bean
    public HandlerInterceptor idempotencyInterceptor(RedisIdempotencyClient client, HandlerMethodUtils utils) {
        return new IdempotentControlInterceptor(client, utils);
    }

    @Bean
    public HandlerMethodUtils methodUtils(IdempotencyProperties properties) {
        return new HandlerMethodUtils(properties);
    }

    @Bean
    public RedisIdempotencyClient redisIdempotencyClient(
            RedisTemplate<String, IdempotencyValue> redisIdempotencyTemplate) {
        return new RedisIdempotencyClient(redisIdempotencyTemplate);
    }

    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> contentCachingFilterRegistration() {
        FilterRegistrationBean<OncePerRequestFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new ContentCachingFilter());
        registration.addUrlPatterns("/*"); // это не ок, но исправление выглядит сложно
        return registration;
    }

    @Bean
    RedisTemplate<String, IdempotencyValue> redisIdempotencyTemplate(
            RedisConnectionFactory redisConnectionFactory) {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer(IdempotencyValue.class);

        RedisTemplate<String, IdempotencyValue> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);

        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        return template;
    }
}

package com.example.idempotency;

import com.example.idempotency.utils.DefaultRequestEqualityChecker;
import com.example.idempotency.utils.HeaderKeyExtractor;
import com.example.idempotency.utils.KeyExtractor;
import com.example.idempotency.utils.RequestEqualityChecker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for methods that need to be idempotent
 * Default values are taken from idempotency properties
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {
    long lockExpireTimeSeconds() default -1;
    long timeToLiveSeconds() default -1;
    String headerName() default "";

    Class<? extends KeyExtractor> keyExtractor() default HeaderKeyExtractor.class;

    Class<? extends RequestEqualityChecker> requestEqualityChecker() default DefaultRequestEqualityChecker.class;
}

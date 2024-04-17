package com.example.idempotency.utils;

public class DefaultConstants {
    public static final String HEADER_NAME = "Idempotency-Key";
    public static final long TIME_TO_LIVE_SECONDS = 30L;
    public static final long LOCK_EXPIRED_SECONDS = 30L;
}

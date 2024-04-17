package com.example.idempotency.utils;

import com.example.idempotency.serializer.RequestSerializable;
import com.example.idempotency.serializer.ResponseSerializable;

public record IdempotencyValue(RequestSerializable request, ResponseSerializable response, boolean isDone) {
}

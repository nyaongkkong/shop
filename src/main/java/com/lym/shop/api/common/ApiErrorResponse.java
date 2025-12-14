package com.lym.shop.api.common;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiErrorResponse(
        String code,
        String message,
        Map<String, String> fieldErrors,
        LocalDateTime timestamp
) {
    public static ApiErrorResponse of(String code, String message) {
        return new ApiErrorResponse(code, message, null, LocalDateTime.now());
    }

    public static ApiErrorResponse of(String code, String message, Map<String, String> fieldErrors) {
        return new ApiErrorResponse(code, message, fieldErrors, LocalDateTime.now());
    }
}

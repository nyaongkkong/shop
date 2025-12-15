package com.lym.shop.api.common;

public record ApiResponse<T>(
        boolean success,
        T data,
        ApiErrorResponse error
) {
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> fail(ApiErrorResponse error) {
        return new ApiResponse<>(false, null, error);
    }
}

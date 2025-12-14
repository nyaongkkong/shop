package com.lym.shop.api.auth.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        String tokenType
) {}

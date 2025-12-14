package com.lym.shop.api.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(
        @NotBlank(message = "refreshToken이 필요합니다.")
        String refreshToken
) {}

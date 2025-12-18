package com.lym.shop.api.category.dto;

import java.math.BigDecimal;

public record CategoryProductItemResponse(
        Long id,
        String name,
        String thumbnailUrl,
        BigDecimal price
) {}

package com.lym.shop.api.product.dto;

import com.lym.shop.domain.product.Product;

import java.math.BigDecimal;

public record ProductCardDto(
        Long id,
        String name,
        String slug,
        String thumbnailUrl,
        BigDecimal price,
        String brandName
) {
    public static ProductCardDto from(Product product) {
        return new ProductCardDto(
                product.getId(),
                product.getName(),
                product.getSlug(),
                product.getThumbnailUrl(),
                product.getPrice(),
                product.getBrand().getName()
        );
    }
}
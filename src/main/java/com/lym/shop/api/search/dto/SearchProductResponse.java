package com.lym.shop.api.search.dto;

import com.lym.shop.domain.product.Product;

import java.math.BigDecimal;

public record SearchProductResponse(
        Long id,
        String name,
        String slug,
        String thumbnailUrl,
        BigDecimal price,
        String brandName
) {

    public static SearchProductResponse from(Product product) {

        return new SearchProductResponse(
                product.getId(),
                product.getName(),
                product.getSlug(),
                product.getThumbnailUrl(),
                product.getPrice(),
                product.getBrand().getName()
        );
    }
}
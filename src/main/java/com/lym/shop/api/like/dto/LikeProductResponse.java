package com.lym.shop.api.like.dto;

import com.lym.shop.domain.product.Product;

import java.math.BigDecimal;

public record LikeProductResponse(
        Long id,
        String name,
        String slug,
        String thumbnailUrl,
        BigDecimal price,
        String brandName
) {

    public static LikeProductResponse from(Product p) {
        return new LikeProductResponse(
                p.getId(),
                p.getName(),
                p.getSlug(),
                p.getThumbnailUrl(),
                p.getPrice(),
                p.getBrand().getName()
        );
    }
}

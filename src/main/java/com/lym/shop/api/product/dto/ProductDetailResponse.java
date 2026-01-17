package com.lym.shop.api.product.dto;

import com.lym.shop.domain.product.Product;

import java.math.BigDecimal;

public record ProductDetailResponse(
        Long id,
        String name,
        String slug,
        BigDecimal price,
        String thumbnailUrl,
        BrandInfo brand,
        CategoryInfo category
) {

    public static ProductDetailResponse from(Product p) {
        return new ProductDetailResponse(
                p.getId(),
                p.getName(),
                p.getSlug(),
                p.getPrice(),
                p.getThumbnailUrl(),
                new BrandInfo(
                        p.getBrand().getName(),
                        p.getBrand().getSlug()
                ),
                new CategoryInfo(
                        p.getPrimaryCategory().getName(),
                        p.getPrimaryCategory().getSlug()
                )
        );
    }

    public record BrandInfo(String name, String slug) {}
    public record CategoryInfo(String name, String slug) {}
}
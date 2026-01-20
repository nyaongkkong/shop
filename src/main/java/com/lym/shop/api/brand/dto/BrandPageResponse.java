package com.lym.shop.api.brand.dto;

import com.lym.shop.domain.brand.Brand;
import com.lym.shop.domain.product.Product;

import java.math.BigDecimal;
import java.util.List;

public record BrandPageResponse(
        BrandInfo brand,
        List<ProductCard> products
) {

    public static BrandPageResponse from(Brand brand, List<Product> products) {
        return new BrandPageResponse(
                BrandInfo.from(brand),
                products.stream().map(ProductCard::from).toList()
        );
    }

    public record BrandInfo(
            Long id,
            String name,
            String slug,
            String logoUrl
    ) {
        public static BrandInfo from(Brand b) {
            return new BrandInfo(
                    b.getId(),
                    b.getName(),
                    b.getSlug(),
                    b.getLogoUrl()
            );
        }
    }

    public record ProductCard(
            Long id,
            String name,
            String slug,
            String thumbnailUrl,
            BigDecimal price
    ) {
        public static ProductCard from(Product p) {
            return new ProductCard(
                    p.getId(),
                    p.getName(),
                    p.getSlug(),
                    p.getThumbnailUrl(),
                    p.getPrice()
            );
        }
    }
}
package com.lym.shop.api.search.dto;

import com.lym.shop.domain.brand.Brand;
import com.lym.shop.domain.product.Product;
import java.math.BigDecimal;
import java.util.List;

public record SearchResponse(
        String query,
        List<BrandCard> brands,
        List<ProductCard> products
) {
    public static SearchResponse from(SearchResult result) {
        return new SearchResponse(
                result.query(),
                result.brands().stream().map(BrandCard::from).toList(),
                result.products().getContent().stream().map(ProductCard::from).toList()
        );
    }

    public record BrandCard(
            Long id,
            String name,
            String slug,
            String logoUrl
    ) {
        public static BrandCard from(Brand b) {
            return new BrandCard(b.getId(), b.getName(), b.getSlug(), b.getLogoUrl());
        }
    }

    public record ProductCard(
            Long id,
            String name,
            String slug,
            String thumbnailUrl,
            BigDecimal price,
            String brandName
    ) {
        public static ProductCard from(Product p) {
            return new ProductCard(
                    p.getId(),
                    p.getName(),
                    p.getSlug(),
                    p.getThumbnailUrl(),
                    p.getPrice(),
                    p.getBrand().getName()
            );
        }
    }
}

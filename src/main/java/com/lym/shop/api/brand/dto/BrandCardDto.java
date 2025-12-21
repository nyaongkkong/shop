package com.lym.shop.api.brand.dto;

import com.lym.shop.domain.brand.Brand;

public record BrandCardDto(
        Long id,
        String name,
        String slug,
        String logoUrl
) {
    public static BrandCardDto from(Brand brand) {
        return new BrandCardDto(brand.getId(), brand.getName(), brand.getSlug(), brand.getLogoUrl());
    }
}
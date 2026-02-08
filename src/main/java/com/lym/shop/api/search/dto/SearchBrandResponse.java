package com.lym.shop.api.search.dto;

import com.lym.shop.domain.brand.Brand;

public record SearchBrandResponse(
        Long id,
        String name,
        String slug,
        String logoUrl
) {

    public static SearchBrandResponse from(Brand b) {
        return new SearchBrandResponse(
                b.getId(),
                b.getName(),
                b.getSlug(),
                b.getLogoUrl()
        );
    }
}

package com.lym.shop.api.category.dto;

import com.lym.shop.domain.category.Category;

public record CategoryDto(
        Long id,
        String name,
        String slug
) {
    static CategoryDto from(Category category) {
        return new CategoryDto(category.getId(), category.getName(), category.getSlug());
    }
}
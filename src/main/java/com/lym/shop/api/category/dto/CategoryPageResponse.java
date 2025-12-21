package com.lym.shop.api.category.dto;

import com.lym.shop.api.brand.dto.BrandCardDto;
import com.lym.shop.api.product.dto.ProductCardDto;
import com.lym.shop.domain.brand.Brand;
import com.lym.shop.domain.category.Category;
import com.lym.shop.domain.product.Product;

import java.util.List;

public record CategoryPageResponse(
        CategoryDto category,
        List<ProductCardDto> previewProducts,
        List<BrandCardDto> brands
) {
    public static CategoryPageResponse from(Category category, List<Product> products, List<Brand> brands) {
        return new CategoryPageResponse(
                CategoryDto.from(category),
                products.stream().map(ProductCardDto::from).toList(),
                brands.stream().map(BrandCardDto::from).toList()
        );
    }
}
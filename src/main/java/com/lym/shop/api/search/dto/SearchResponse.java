package com.lym.shop.api.search.dto;

import com.lym.shop.domain.brand.Brand;
import com.lym.shop.domain.product.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public record SearchResponse(
        List<SearchProductResponse> products,
        List<SearchBrandResponse> brands,
        int totalPages,
        long totalElements
) {

    public static SearchResponse from(
            Page<Product> page,
            List<Brand> brands
    ) {

        return new SearchResponse(
                page.getContent()
                        .stream()
                        .map(SearchProductResponse::from)
                        .toList(),

                brands.stream()
                        .map(SearchBrandResponse::from)
                        .toList(),

                page.getTotalPages(),
                page.getTotalElements()
        );
    }
}

package com.lym.shop.api.search.dto;

import com.lym.shop.domain.product.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public record SearchResponse(
        List<SearchProductResponse> products,
        int totalPages,
        long totalElements
) {

    public static SearchResponse from(Page<Product> page) {

        return new SearchResponse(
                page.getContent()
                        .stream()
                        .map(SearchProductResponse::from)
                        .toList(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }
}
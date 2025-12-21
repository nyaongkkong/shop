package com.lym.shop.api.search.dto;

import com.lym.shop.domain.brand.Brand;
import com.lym.shop.domain.product.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public record SearchResult(
        String query,
        List<Brand> brands,
        Page<Product> products
) {}

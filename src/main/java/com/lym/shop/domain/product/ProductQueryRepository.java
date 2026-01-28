package com.lym.shop.domain.product;

import com.lym.shop.domain.category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductQueryRepository {
    Page<Product> searchProducts(
            Category category,
            String brandSlug,
            String keyword,
            ProductSortType sortType,
            Pageable pageable
    );
}

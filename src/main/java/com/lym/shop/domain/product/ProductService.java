package com.lym.shop.domain.product;

import com.lym.shop.domain.category.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Page<Product> getProductsByCategory(Category category, Pageable pageable) {
        return productRepository.findByPrimaryCategoryAndStatus(
                category,
                ProductStatus.ACTIVE,
                pageable
        );
    }
}

package com.lym.shop.domain.product;

import com.lym.shop.domain.category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByPrimaryCategoryAndStatus(
            Category primaryCategory,
            ProductStatus status,
            Pageable pageable
    );
}

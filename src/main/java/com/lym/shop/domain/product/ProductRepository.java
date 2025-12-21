package com.lym.shop.domain.product;

import com.lym.shop.domain.brand.Brand;
import com.lym.shop.domain.category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByPrimaryCategoryAndStatus(Category primaryCategory, ProductStatus status, Pageable pageable);

    // 카테고리에 상품이 있는 브랜드 목록 (중복 제거)
    @Query("""
        select distinct p.brand
        from Product p
        where p.primaryCategory = :category
          and p.status = :status
          and p.brand.active = true
        order by p.brand.name asc
    """)
    List<Brand> findDistinctBrandsByCategory(
            @Param("category") Category category,
            @Param("status") ProductStatus status,
            Pageable pageable
    );

    java.util.Optional<Product> findBySlug(String slug);

    // 검색용: 상품명에 q 포함 + ACTIVE
    Page<Product> findByStatusAndNameContainingIgnoreCaseOrderByCreatedAtDesc(
            ProductStatus status,
            String q,
            Pageable pageable
    );
}

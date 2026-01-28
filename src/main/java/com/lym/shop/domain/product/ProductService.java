package com.lym.shop.domain.product;

import com.lym.shop.domain.category.Category;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    /**
     * QueryDSL 기반 통합 상품 조회
     */
    public Page<Product> getProducts(
            Category category,
            String brandSlug,
            String keyword,
            ProductSortType sortType,
            Pageable pageable
    ) {

        return productRepository.searchProducts(
                category,
                brandSlug,
                keyword,
                sortType,
                pageable
        );
    }

    /**
     * 상품 상세 (slug)
     */
    public Product getBySlug(String slug) {
        return productRepository
                .findBySlugAndStatus(slug, ProductStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));
    }

    /**
     * 상품 단건 (id)
     */
    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));
    }
}

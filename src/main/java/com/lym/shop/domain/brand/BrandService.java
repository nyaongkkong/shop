package com.lym.shop.domain.brand;

import com.lym.shop.domain.category.Category;
import com.lym.shop.domain.product.ProductRepository;
import com.lym.shop.domain.product.ProductStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;

    public List<Brand> getBrandsHavingProductsIn(Category category, int limit) {
        return productRepository.findDistinctBrandsByCategory(
                category,
                ProductStatus.ACTIVE,
                PageRequest.of(0, limit)
        );
    }

    public Brand getBySlug(String slug) {
        return brandRepository
                .findBySlugAndActiveTrue(slug)
                .orElseThrow(() -> new EntityNotFoundException("브랜드를 찾을 수 없습니다."));
    }
}

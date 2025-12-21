package com.lym.shop.domain.brand;

import com.lym.shop.domain.category.Category;
import com.lym.shop.domain.product.ProductRepository;
import com.lym.shop.domain.product.ProductStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final ProductRepository productRepository;

    public List<Brand> getBrandsHavingProductsIn(Category category, int limit) {
        return productRepository.findDistinctBrandsByCategory(
                category,
                ProductStatus.ACTIVE,
                PageRequest.of(0, limit)
        );
    }
}

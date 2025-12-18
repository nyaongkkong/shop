package com.lym.shop.config.init;

import com.lym.shop.domain.brand.Brand;
import com.lym.shop.domain.brand.BrandRepository;
import com.lym.shop.domain.category.Category;
import com.lym.shop.domain.category.CategoryRepository;
import com.lym.shop.domain.product.Product;
import com.lym.shop.domain.product.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Profile({"local", "dev"})
public class DataInitializer {

    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    @PostConstruct
    void init() {
        if (productRepository.count() > 0) {
            return;
        }

        // 1️⃣ Category
        Category sneakers = categoryRepository.findBySlug("sneakers")
                .orElseGet(() ->
                        categoryRepository.save(
                                Category.create(null, "스니커즈", "sneakers")
                        )
                );

        // 2️⃣ Brand
        Brand nike = brandRepository.findBySlug("nike")
                .orElseGet(() ->
                        brandRepository.save(
                                Brand.create("나이키", "nike", null, "NIKE")
                        )
                );

        Brand adidas = brandRepository.findBySlug("adidas")
                .orElseGet(() ->
                        brandRepository.save(
                                Brand.create("아디다스", "adidas", null, "ADIDAS")
                        )
                );

        Brand nb = brandRepository.findBySlug("new-balance")
                .orElseGet(() ->
                        brandRepository.save(
                                Brand.create("뉴발란스", "new-balance", null, "NEW BALANCE")
                        )
                );

        // 3️⃣ Product
        productRepository.save(
                Product.create(
                        nike,
                        sneakers,
                        "나이키 에어포스 1",
                        "nike-air-force-1",
                        "https://dummyimage.com/300x300/000/fff&text=AirForce1",
                        new BigDecimal("139000")
                )
        );

        productRepository.save(
                Product.create(
                        adidas,
                        sneakers,
                        "아디다스 슈퍼스타",
                        "adidas-superstar",
                        "https://dummyimage.com/300x300/000/fff&text=Superstar",
                        new BigDecimal("129000")
                )
        );

        productRepository.save(
                Product.create(
                        nb,
                        sneakers,
                        "뉴발란스 992",
                        "nb-992",
                        "https://dummyimage.com/300x300/000/fff&text=NB992",
                        new BigDecimal("259000")
                )
        );
    }
}

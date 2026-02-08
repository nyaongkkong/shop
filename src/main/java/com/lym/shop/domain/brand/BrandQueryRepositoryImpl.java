package com.lym.shop.domain.brand;

import com.lym.shop.domain.product.ProductStatus;
import com.lym.shop.domain.product.QProduct;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BrandQueryRepositoryImpl
        implements BrandQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Brand> findBrandsByProductKeyword(String keyword, int limit) {

        QProduct product = QProduct.product;
        QBrand brand = QBrand.brand;

        return queryFactory
                .selectDistinct(brand)
                .from(product)
                .join(product.brand, brand)
                .where(
                        product.status.eq(ProductStatus.ACTIVE)
                                .and(product.name.containsIgnoreCase(keyword))
                )
                .orderBy(brand.name.asc())
                .limit(limit)
                .fetch();
    }
}
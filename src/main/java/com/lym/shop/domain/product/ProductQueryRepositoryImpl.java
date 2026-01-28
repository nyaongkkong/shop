package com.lym.shop.domain.product;

import com.lym.shop.domain.category.Category;
import com.lym.shop.domain.like.QProductLike;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class ProductQueryRepositoryImpl implements ProductQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> searchProducts(
            Category category,
            String brandSlug,
            String keyword,
            ProductSortType sortType,
            Pageable pageable
    ) {
        QProduct product = QProduct.product;
        QProductLike productLike = QProductLike.productLike;

        BooleanBuilder where = new BooleanBuilder();

        // 기본 조건
        where.and(product.status.eq(ProductStatus.ACTIVE));

        // 카테고리 필터
        if (category != null) {
            where.and(product.primaryCategory.eq(category));
        }

        // 브랜드 필터
        if (brandSlug != null) {
            where.and(product.brand.slug.eq(brandSlug));
        }

        // 검색어
        if (keyword != null && !keyword.isBlank()) {
            where.and(
                    product.name.containsIgnoreCase(keyword)
                            .or(product.brand.name.containsIgnoreCase(keyword))
            );
        }

        // 메인 쿼리
        JPAQuery<Product> query =
                queryFactory
                        .selectFrom(product)
                        .leftJoin(product.likes, productLike)
                        .fetchJoin()
                        .where(where)
                        .groupBy(product);

        // 정렬 적용
        applySort(query, sortType, productLike, product);

        // 페이징
        List<Product> content =
                query
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        // count 쿼리
        Long total =
                queryFactory
                        .select(product.count())
                        .from(product)
                        .where(where)
                        .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private void applySort(
            JPAQuery<Product> query,
            ProductSortType sortType,
            QProductLike productLike,
            QProduct product
    ) {

        if (sortType == null) {
            sortType = ProductSortType.LATEST;
        }

        switch (sortType) {
            case POPULAR ->
                    query.orderBy(productLike.count().desc());

            case PRICE_LOW ->
                    query.orderBy(product.price.asc());

            case PRICE_HIGH ->
                    query.orderBy(product.price.desc());

            default ->
                    query.orderBy(product.createdAt.desc());
        }
    }
}

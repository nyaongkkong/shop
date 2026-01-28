package com.lym.shop.domain.product;

public enum ProductSortType {
    LATEST,      // 최신순 (createdAt desc)
    POPULAR,     // 인기순 (찜/좋아요 desc)
    PRICE_LOW,   // 가격 낮은순
    PRICE_HIGH   // 가격 높은순
}

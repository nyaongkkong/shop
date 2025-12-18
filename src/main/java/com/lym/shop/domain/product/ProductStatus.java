package com.lym.shop.domain.product;

public enum ProductStatus {
    ACTIVE,     // 노출/판매
    HIDDEN,     // 숨김(운영)
    SOLD_OUT    // 품절(표시 정책은 서비스에서)
}
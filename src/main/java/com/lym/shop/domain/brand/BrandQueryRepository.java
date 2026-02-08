package com.lym.shop.domain.brand;

import java.util.List;

public interface BrandQueryRepository {
    List<Brand> findBrandsByProductKeyword(String keyword, int limit);
}

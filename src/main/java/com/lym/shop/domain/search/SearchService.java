package com.lym.shop.domain.search;

import com.lym.shop.api.search.dto.SearchResult;
import com.lym.shop.domain.brand.Brand;
import com.lym.shop.domain.brand.BrandRepository;
import com.lym.shop.domain.product.Product;
import com.lym.shop.domain.product.ProductRepository;
import com.lym.shop.domain.product.ProductStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    public SearchResult search(String q) {
        String query = normalize(q);

        if (query.isBlank()) {
            return new SearchResult("", List.of(), Page.empty());
        }

        // 1) 브랜드: 정확 일치(있으면 맨 앞) + contains
        List<Brand> brandList = new ArrayList<>();

        brandRepository.findByActiveTrueAndNameIgnoreCase(query)
                .ifPresent(brandList::add);

        List<Brand> contains = brandRepository
                .findByActiveTrueAndNameContainingIgnoreCaseOrderByNameAsc(query, PageRequest.of(0, 8));

        // 중복 제거(정확일치가 이미 들어갔을 수 있음)
        for (Brand b : contains) {
            boolean exists = brandList.stream().anyMatch(x -> Objects.equals(x.getId(), b.getId()));
            if (!exists) brandList.add(b);
            if (brandList.size() >= 8) break;
        }

        // 2) 상품: 10개 프리뷰(= 1~2줄)
        Page<Product> productPage = productRepository
                .findByStatusAndNameContainingIgnoreCaseOrderByCreatedAtDesc(
                        ProductStatus.ACTIVE,
                        query,
                        PageRequest.of(0, 10)
                );

        return new SearchResult(query, brandList, productPage);
    }

    private String normalize(String q) {
        return q == null ? "" : q.trim();
    }
}
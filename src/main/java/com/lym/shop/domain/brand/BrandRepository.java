package com.lym.shop.domain.brand;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findBySlug(String slug);

    // 검색용: 이름에 q 포함 + 활성
    List<Brand> findByActiveTrueAndNameContainingIgnoreCaseOrderByNameAsc(String q, Pageable pageable);

    // "정확히 일치" 우선 처리용(선택)
    Optional<Brand> findByActiveTrueAndNameIgnoreCase(String name);

}

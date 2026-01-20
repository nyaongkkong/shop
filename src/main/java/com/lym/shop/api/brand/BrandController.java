package com.lym.shop.api.brand;

import com.lym.shop.api.brand.dto.BrandPageResponse;
import com.lym.shop.api.common.ApiResponse;
import com.lym.shop.domain.brand.Brand;
import com.lym.shop.domain.brand.BrandService;
import com.lym.shop.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/brands")
public class BrandController {
    private final BrandService brandService;
    private final ProductService productService;

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<BrandPageResponse>> brandPage(@PathVariable String slug) {
        Brand brand = brandService.getBySlug(slug);

        var products = productService.getProductsByBrand(
                brand,
                PageRequest.of(0, 20, Sort.by("createdAt").descending())
        );

        BrandPageResponse data = BrandPageResponse.from(brand, products.getContent());

        return ResponseEntity.ok(ApiResponse.ok(data));
    }
}

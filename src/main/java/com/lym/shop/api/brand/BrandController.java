package com.lym.shop.api.brand;

import com.lym.shop.api.brand.dto.BrandPageResponse;
import com.lym.shop.api.common.ApiResponse;
import com.lym.shop.domain.brand.Brand;
import com.lym.shop.domain.brand.BrandService;
import com.lym.shop.domain.product.Product;
import com.lym.shop.domain.product.ProductService;
import com.lym.shop.domain.product.ProductSortType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/brands")
public class BrandController {

    private final BrandService brandService;
    private final ProductService productService;

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<BrandPageResponse>> brandPage(
            @PathVariable String slug,
            @RequestParam(defaultValue = "LATEST") ProductSortType sort,
            @RequestParam(defaultValue = "0") int page
    ) {
        Brand brand = brandService.getBySlug(slug);

        Page<Product> products = productService.getProducts(
                        null,       // category 없음
                        slug,       // brandSlug
                        null,       // keyword 없음
                        sort,
                        PageRequest.of(page, 20)
                );

        BrandPageResponse data = BrandPageResponse.from(brand, products.getContent());

        return ResponseEntity.ok(ApiResponse.ok(data));
    }
}
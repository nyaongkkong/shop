package com.lym.shop.api.product;

import com.lym.shop.api.common.ApiResponse;
import com.lym.shop.api.product.dto.ProductDetailResponse;
import com.lym.shop.domain.product.Product;
import com.lym.shop.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> detail(@PathVariable String slug) {
        Product product = productService.getBySlug(slug);

        ProductDetailResponse data = ProductDetailResponse.from(product);

        return ResponseEntity.ok(ApiResponse.ok(data));
    }
}

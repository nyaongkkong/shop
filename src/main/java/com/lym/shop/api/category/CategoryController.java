package com.lym.shop.api.category;

import com.lym.shop.api.category.dto.CategoryPageResponse;
import com.lym.shop.api.common.ApiResponse;
import com.lym.shop.domain.brand.Brand;
import com.lym.shop.domain.brand.BrandService;
import com.lym.shop.domain.category.Category;
import com.lym.shop.domain.category.CategoryService;
import com.lym.shop.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final BrandService brandService;

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<CategoryPageResponse>> categoryPage(@PathVariable String slug) {
        Category category = categoryService.getBySlug(slug);

        var previewProducts = productService.getProductsByCategory(
                category,
                PageRequest.of(0, 10, Sort.by("createdAt").descending())
        );

        List<Brand> brands = brandService.getBrandsHavingProductsIn(category, 20);

        CategoryPageResponse data = CategoryPageResponse.from(category, previewProducts.getContent(), brands);

        return ResponseEntity.ok(ApiResponse.ok(data));
    }

}

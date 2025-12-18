package com.lym.shop.api.category;

import com.lym.shop.api.category.dto.CategoryProductItemResponse;
import com.lym.shop.domain.category.Category;
import com.lym.shop.domain.category.CategoryService;
import com.lym.shop.domain.product.Product;
import com.lym.shop.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final ProductService productService;

    @GetMapping("/{slug}")
    public String categoryPage(@PathVariable String slug, @RequestParam(defaultValue = "0") int page, Model model) {
        Category category = categoryService.getBySlug(slug);

        Page<Product> products = productService.getProductsByCategory(
                category,
                PageRequest.of(page, 20, Sort.by("createdAt").descending())
        );

        model.addAttribute("category", category);
        model.addAttribute("products", products);

        return "category/category-products";
    }
}

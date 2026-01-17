package com.lym.shop.api.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "index";
    }
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }
    @GetMapping("/signup")
    public String signupPage() {
        return "auth/signup";
    }
    @GetMapping("/categories/{slug}")
    public String categoryPage(@PathVariable String slug) {
        return "category/category-products";
    }
    @GetMapping("/search")
    public String page() {
        return "search/search";
    }

    @GetMapping("/products/{slug}")
    public String productPage(@PathVariable String slug) {
        return "product/product-detail";
    }
}
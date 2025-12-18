package com.lym.shop.api.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
}
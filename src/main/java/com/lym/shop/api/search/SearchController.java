package com.lym.shop.api.search;

import com.lym.shop.api.search.dto.SearchResult;
import com.lym.shop.domain.search.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public String searchPage(@RequestParam(name = "q", required = false) String q, Model model) {
        SearchResult result = searchService.search(q);

        model.addAttribute("q", result.query());
        model.addAttribute("brands", result.brands());
        model.addAttribute("products", result.products());

        return "search/search";
    }
}

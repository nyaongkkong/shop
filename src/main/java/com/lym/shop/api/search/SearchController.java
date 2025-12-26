package com.lym.shop.api.search;

import com.lym.shop.api.common.ApiResponse;
import com.lym.shop.api.search.dto.SearchResponse;
import com.lym.shop.api.search.dto.SearchResult;
import com.lym.shop.domain.search.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<ApiResponse<SearchResponse>> search(@RequestParam(name = "q", required = false) String q) {
        SearchResult result = searchService.search(q);
        SearchResponse data = SearchResponse.from(result);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }
}

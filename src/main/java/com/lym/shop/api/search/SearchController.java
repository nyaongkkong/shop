package com.lym.shop.api.search;

import com.lym.shop.api.common.ApiResponse;
import com.lym.shop.api.search.dto.SearchResponse;
import com.lym.shop.api.search.dto.SearchResult;
import com.lym.shop.domain.product.Product;
import com.lym.shop.domain.product.ProductService;
import com.lym.shop.domain.product.ProductSortType;
import com.lym.shop.domain.search.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<SearchResponse>> search(
            @RequestParam(name = "q") String keyword,
            @RequestParam(defaultValue = "LATEST") ProductSortType sort,
            @RequestParam(defaultValue = "0") int page
    ) {
        Page<Product> products = productService.getProducts(
                        null,      // category 없음
                        null,      // brand 없음
                        keyword,   // 검색어
                        sort,
                        PageRequest.of(page, 20)
                );

        SearchResponse data = SearchResponse.from(products);

        return ResponseEntity.ok(ApiResponse.ok(data));
    }
}

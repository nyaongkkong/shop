package com.lym.shop.domain.like;

import com.lym.shop.api.like.dto.LikeProductResponse;
import com.lym.shop.domain.member.Member;
import com.lym.shop.domain.product.Product;
import com.lym.shop.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductLikeService {
    private final ProductLikeRepository productLikeRepository;
    private final ProductService productService;

    /**
     * 찜 토글
     */
    @Transactional
    public boolean toggleLike(Member member, Long productId) {
        Product product = productService.getById(productId);

        return productLikeRepository
                .findByUserAndProduct(member, product)
                .map(existing -> {
                    // 이미 찜 → 삭제
                    productLikeRepository.delete(existing);
                    return false; // UNLIKE 상태
                })
                .orElseGet(() -> {
                    // 찜 안됨 → 생성
                    ProductLike like = ProductLike.of(member, product);
                    productLikeRepository.save(like);
                    return true; // LIKE 상태
                });
    }

    /**
     * 찜 여부 확인
     */
    public boolean isLiked(Member member, Product product) {
        return productLikeRepository.existsByUserAndProduct(member, product);
    }

    /**
     * 상품 찜 개수
     */
    public long getLikeCount(Product product) {
        return productLikeRepository.countByProduct(product);
    }

    public Product getProduct(Long productId) {
        return productService.getById(productId);
    }

    @Transactional(readOnly = true)
    public List<LikeProductResponse> getMyLikes(Member member) {
        return productLikeRepository
                .findAllByUserOrderByCreatedAtDesc(member)
                .stream()
                .map(like -> LikeProductResponse.from(like.getProduct()))
                .toList();
    }
}

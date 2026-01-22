package com.lym.shop.api.like;

import com.lym.shop.api.common.ApiResponse;
import com.lym.shop.api.like.dto.LikeProductResponse;
import com.lym.shop.api.like.dto.LikeResponse;
import com.lym.shop.config.MemberDetails;
import com.lym.shop.domain.like.ProductLikeService;
import com.lym.shop.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductLikeController {
    private final ProductLikeService productLikeService;

    @PostMapping("/{productId}/like")
    public ResponseEntity<ApiResponse<LikeResponse>> toggleLike(
            @PathVariable Long productId,
            @AuthenticationPrincipal MemberDetails memberDetails
    ) {
        Member member = memberDetails.getMember();

        boolean liked = productLikeService.toggleLike(member, productId);

        return ResponseEntity.ok(ApiResponse.ok(new LikeResponse(liked)));
    }

    @GetMapping("/{productId}/like")
    public ResponseEntity<ApiResponse<LikeResponse>> getLikeStatus(
            @PathVariable Long productId,
            @AuthenticationPrincipal MemberDetails memberDetails
    ) {
        Member member = memberDetails.getMember();

        boolean liked = productLikeService.isLiked(member, productLikeService.getProduct(productId));

        return ResponseEntity.ok(ApiResponse.ok(new LikeResponse(liked)));
    }

    @GetMapping("/me/likes")
    public ResponseEntity<ApiResponse<List<LikeProductResponse>>> myLikes(
            @AuthenticationPrincipal MemberDetails memberDetails
    ) {

        Member member = memberDetails.getMember();

        List<LikeProductResponse> data = productLikeService.getMyLikes(member);

        return ResponseEntity.ok(ApiResponse.ok(data));
    }

}
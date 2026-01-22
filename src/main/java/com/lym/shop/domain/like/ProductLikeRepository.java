package com.lym.shop.domain.like;

import com.lym.shop.domain.member.Member;
import com.lym.shop.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductLikeRepository extends JpaRepository<ProductLike, Long> {
    Optional<ProductLike> findByUserAndProduct(Member user, Product product);

    boolean existsByUserAndProduct(Member user, Product product);

    long countByProduct(Product product);

    List<ProductLike> findAllByUserOrderByCreatedAtDesc(Member user);
}

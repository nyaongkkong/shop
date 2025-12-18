package com.lym.shop.domain.product;

import com.lym.shop.domain.brand.Brand;
import com.lym.shop.domain.category.Category;
import com.lym.shop.domain.product.ProductStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "product",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_products_slug", columnNames = "slug")
        },
        indexes = {
                @Index(name = "ix_products_brand_id", columnList = "brand_id"),
                @Index(name = "ix_products_status", columnList = "status"),
                @Index(name = "ix_products_name", columnList = "name"),
                @Index(name = "ix_products_price", columnList = "price")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 연관관계는 “읽기 편의”로 두되, 저장/수정은 service에서 brand 검증 후 세팅 권장
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "brand_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_products_brand"))
    private Brand brand;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 220)
    private String slug;

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status = ProductStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "primary_category_id", nullable = false)
    private Category primaryCategory;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    public static Product create(Brand brand, Category primaryCategory, String name, String slug, String thumbnailUrl, BigDecimal price) {
        Product product = new Product();
        product.brand = brand;
        product.primaryCategory = primaryCategory;
        product.name = name;
        product.slug = slug;
        product.thumbnailUrl = thumbnailUrl;
        product.price = price;
        product.status = ProductStatus.ACTIVE;
        product.createdAt = Instant.now();
        product.updatedAt = product.createdAt;
        return product;
    }


    public void update(String name, String slug, String thumbnailUrl, BigDecimal price, ProductStatus status) {
        this.name = name;
        this.slug = slug;
        this.thumbnailUrl = thumbnailUrl;
        this.price = price;
        this.status = status;
        this.updatedAt = Instant.now();
    }

    public void changeBrand(Brand brand) {
        this.brand = brand;
        this.updatedAt = Instant.now();
    }
}

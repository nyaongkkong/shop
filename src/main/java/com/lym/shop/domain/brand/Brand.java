package com.lym.shop.domain.brand;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "brands",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_brands_slug", columnNames = "slug")
        },
        indexes = {
                @Index(name = "ix_brands_name", columnList = "name"),
                @Index(name = "ix_brands_active", columnList = "is_active")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Brand {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 120)
    private String slug;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(length = 2000)
    private String description;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    public static Brand create(String name, String slug, String logoUrl, String description) {
        Brand brand = new Brand();
        brand.name = name;
        brand.slug = slug;
        brand.logoUrl = logoUrl;
        brand.description = description;
        brand.active = true;
        brand.createdAt = Instant.now();
        brand.updatedAt = brand.createdAt;
        return brand;
    }

    public void update(String name, String slug, String logoUrl, String description, boolean active) {
        this.name = name;
        this.slug = slug;
        this.logoUrl = logoUrl;
        this.description = description;
        this.active = active;
        this.updatedAt = Instant.now();
    }
}
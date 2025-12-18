package com.lym.shop.domain.category;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "categories",
        uniqueConstraints = @UniqueConstraint(name = "uk_categories_slug", columnNames = "slug"),
        indexes = {
                @Index(name = "ix_categories_parent_id", columnList = "parent_id"),
                @Index(name = "ix_categories_name", columnList = "name")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", foreignKey = @ForeignKey(name = "fk_categories_parent"))
    private Category parent;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 120)
    private String slug;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    public static Category create(Category parent, String name, String slug) {
        Category category = new Category();
        category.parent = parent;
        category.name = name;
        category.slug = slug;
        category.active = true;
        category.createdAt = Instant.now();
        category.updatedAt = category.createdAt;
        return category;
    }

    public void update(String name, String slug, boolean active) {
        this.name = name;
        this.slug = slug;
        this.active = active;
        this.updatedAt = Instant.now();
    }
}

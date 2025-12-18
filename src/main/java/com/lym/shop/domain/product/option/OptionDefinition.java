package com.lym.shop.domain.product.option;

import com.lym.shop.domain.category.Category;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "option_definitions",
        indexes = {
                @Index(name = "ix_optdef_category_id", columnList = "category_id"),
                @Index(name = "ix_optdef_key", columnList = "opt_key")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionDefinition {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_optdef_category"))
    private Category category;

    @Column(name = "opt_key", nullable = false, length = 50)
    private String key; // ex) "size", "color", "cpu"

    @Column(name = "opt_name", nullable = false, length = 100)
    private String name; // ex) "사이즈", "컬러"

    @Enumerated(EnumType.STRING)
    @Column(name = "value_type", nullable = false, length = 20)
    private OptionValueType valueType; // TEXT, NUMBER, SELECT

    @Column(name = "is_required", nullable = false)
    private boolean required;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    public static OptionDefinition create(Category category, String key, String name,
                                          OptionValueType valueType, boolean required, int sortOrder) {
        OptionDefinition d = new OptionDefinition();
        d.category = category;
        d.key = key;
        d.name = name;
        d.valueType = valueType;
        d.required = required;
        d.sortOrder = sortOrder;
        return d;
    }
}

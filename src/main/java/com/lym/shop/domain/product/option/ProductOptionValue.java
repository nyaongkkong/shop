package com.lym.shop.domain.product.option;

import com.lym.shop.domain.product.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_option_values",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_prod_opt_value",
                columnNames = {"product_id", "option_definition_id"}
        ),
        indexes = {
                @Index(name = "ix_pov_product_id", columnList = "product_id"),
                @Index(name = "ix_pov_optdef_id", columnList = "option_definition_id")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOptionValue {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_pov_product"))
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "option_definition_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_pov_optdef"))
    private OptionDefinition optionDefinition;

    @Column(name = "value_text", length = 1000)
    private String valueText;

    @Column(name = "value_number")
    private Long valueNumber;

    public static ProductOptionValue text(Product p, OptionDefinition d, String value) {
        ProductOptionValue v = new ProductOptionValue();
        v.product = p;
        v.optionDefinition = d;
        v.valueText = value;
        return v;
    }

    public static ProductOptionValue number(Product p, OptionDefinition d, long value) {
        ProductOptionValue v = new ProductOptionValue();
        v.product = p;
        v.optionDefinition = d;
        v.valueNumber = value;
        return v;
    }
}

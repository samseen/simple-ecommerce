package com.samseen.ecommerce.product.model;

import com.samseen.ecommerce.enums.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String productCode;
    private BigDecimal price;
    private String imageUrl;
    @Enumerated(EnumType.STRING)
    private Category category;
    private Integer quantity;
}

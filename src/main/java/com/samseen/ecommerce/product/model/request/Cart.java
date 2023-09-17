package com.samseen.ecommerce.product.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Cart {
    private String productCode;
    private int quantity;
}

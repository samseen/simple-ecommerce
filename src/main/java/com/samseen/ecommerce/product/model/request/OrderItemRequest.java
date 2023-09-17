package com.samseen.ecommerce.product.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class OrderItemRequest {
    private List<Cart> cartList;
}

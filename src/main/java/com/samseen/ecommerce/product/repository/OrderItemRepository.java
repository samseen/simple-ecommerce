package com.samseen.ecommerce.product.repository;

import com.samseen.ecommerce.product.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}

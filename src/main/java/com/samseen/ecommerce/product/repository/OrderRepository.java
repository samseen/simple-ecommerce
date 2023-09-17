package com.samseen.ecommerce.product.repository;

import com.samseen.ecommerce.product.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

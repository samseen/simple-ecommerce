package com.samseen.ecommerce.product.service;

import com.samseen.ecommerce.exceptions.UserNotFoundException;
import com.samseen.ecommerce.product.model.Order;
import com.samseen.ecommerce.product.model.OrderItem;
import com.samseen.ecommerce.product.model.Product;
import com.samseen.ecommerce.product.model.request.Cart;
import com.samseen.ecommerce.product.model.request.OrderItemRequest;
import com.samseen.ecommerce.product.repository.OrderItemRepository;
import com.samseen.ecommerce.product.repository.OrderRepository;
import com.samseen.ecommerce.product.repository.ProductRepository;
import com.samseen.ecommerce.user.User;
import com.samseen.ecommerce.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderProcessingService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;

    // Reduce the product quantity in the database
    // Populate the Order and OrderItem tables
    // Return the order item of the product
    public ResponseEntity<Order> purchaseItem(User user, OrderItemRequest orderItemRequest) {
        Optional<User> dbUser = userRepository.findByUserName(user.getEmail());
        if (dbUser.isEmpty()) {
            throw new UserNotFoundException("User does not exist");
        }

        // Find the product
        // Loop through the products and get them out
        List<Cart> cartList = orderItemRequest.getCartList();
        for (Cart cart : cartList) {
            // Retrieve the item
            Optional<Product> retrievedProduct = productRepository.findByProductCode(cart.getProductCode());
            if (retrievedProduct.isEmpty()) {
                throw new IllegalArgumentException("No product found");
            }
            // Reduce the product in the product database by the quantity ordered
            Product product = retrievedProduct.get();
            product.setQuantity(-1 * cart.getQuantity()); // todo: ensure quantity sent in is not lesser than 1
            productRepository.save(product);

        }
        return null;
    }
}

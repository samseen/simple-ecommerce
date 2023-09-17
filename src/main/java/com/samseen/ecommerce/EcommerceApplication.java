package com.samseen.ecommerce;

import com.samseen.ecommerce.enums.Category;
import com.samseen.ecommerce.product.model.Product;
import com.samseen.ecommerce.product.repository.ProductRepository;
import com.samseen.ecommerce.token.repository.RoleRepository;
import com.samseen.ecommerce.user.entity.Permission;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class EcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

    @Bean
    public CommandLineRunner initializeDatabase(RoleRepository roleRepository, ProductRepository productRepository) {
        return args -> {
            // Check if the database is empty; if so, populate it
            if (roleRepository.count() == 0) {
                Permission userPermission = new Permission();
                userPermission.setName("USER");

                Permission managerPermission = new Permission();
                managerPermission.setName("MANAGER");

                roleRepository.save(userPermission);
                roleRepository.save(managerPermission);

                System.out.println("Database populated with permissions.");
            }

            if (productRepository.count() == 0) {
                Product productKnife = Product.builder()
                        .name("Knife")
                        .description("A simple kitchen knife")
                        .price(BigDecimal.valueOf(5))
                        .productCode("SKU1350009")
                        .category(Category.KITCHEN)
                        .quantity(100)
                        .build();

                Product productKettle = Product.builder()
                        .name("Kettle")
                        .description("Boiling Kettle")
                        .price(BigDecimal.valueOf(13))
                        .productCode("SKU1350010")
                        .category(Category.KITCHEN)
                        .quantity(100)
                        .build();

                productRepository.save(productKnife);
                productRepository.save(productKettle);

                System.out.println("Database populated with products.");
            }
        };
    }
}

package com.samseen.ecommerce;

import com.samseen.ecommerce.token.repositories.RoleRepository;
import com.samseen.ecommerce.user.entity.Permission;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

    @Bean
    public CommandLineRunner initializeDatabase(RoleRepository roleRepository) {
        return args -> {
            // Check if the database is empty; if so, populate it
            if (roleRepository.count() == 0) {
                Permission userPermission = new Permission();
                userPermission.setName("USER");

                Permission managerPermission = new Permission();
                managerPermission.setName("MANAGER");

                roleRepository.save(userPermission);
                roleRepository.save(managerPermission);

                System.out.println("Database populated with sample data.");
            }
        };
    }
}

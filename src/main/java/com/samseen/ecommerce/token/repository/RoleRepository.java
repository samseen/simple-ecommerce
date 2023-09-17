package com.samseen.ecommerce.token.repository;

import com.samseen.ecommerce.user.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Permission, Long> {
    Permission getRoleByName(String roleName);
}

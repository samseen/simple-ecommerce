package com.samseen.ecommerce.token.repositories;

import com.samseen.ecommerce.token.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("SELECT t FROM Token t inner join User u on t.user.id = u.id " +
            "WHERE u.id = :userId AND (t.expired = false OR t.revoked = false )")
    List<Token> findAllValidTokensByUser(Long userId);

    Optional<Token> findByToken(String token);
}

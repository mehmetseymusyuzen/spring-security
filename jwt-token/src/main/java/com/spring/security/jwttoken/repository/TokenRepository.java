package com.spring.security.jwttoken.repository;

import com.spring.security.jwttoken.model.Token;
import com.spring.security.jwttoken.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, String> {

 Optional<Token> findByToken(final String token);

 List<Token> findTokenByUserAndExpiredIsFalseAndRevokedIsFalse(final User user);

 // Or With @Query Annotations
/*    @Query("""
                    SELECT t FROM  Token t INNER JOIN User u ON t.user.id = u.id
                    WHERE u.id = :userId AND (t.expired = false  OR t.revoked= false)
            """)
    List<Token> findAllValidTokensByUser(final User user);*/
}

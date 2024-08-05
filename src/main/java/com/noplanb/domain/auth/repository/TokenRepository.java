package com.noplanb.domain.auth.repository;

import com.noplanb.domain.auth.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByEmail(String email);
    Optional<Token> findByRefreshToken(String refreshToken);

}
package com.noplanb.domain.auth.repository;

import com.noplanb.domain.auth.domain.Token;
import com.noplanb.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByEmail(String email);
    Optional<Token> findByRefreshToken(String refreshToken);

}
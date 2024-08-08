package com.noplanb.domain.auth.domain;


import com.noplanb.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Token extends BaseEntity {

    @Id
    @Column(name = "email", nullable = false)
    private String email;

    @Lob
    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    public Token updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    @Builder
    public Token(String email, String refreshToken) {
        this.email = email;
        this.refreshToken = refreshToken;
    }
}
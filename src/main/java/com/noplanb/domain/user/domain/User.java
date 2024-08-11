package com.noplanb.domain.user.domain;

import com.noplanb.domain.character.domain.Character;
import com.noplanb.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String userName;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Provider provider;


    @Enumerated(EnumType.STRING)
    private Role role;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "character_id", unique = true)
//    private Character character;

    @Builder
    public User(String userName, String email, String password, Provider provider, Role role) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.role = role;
    }
}

package com.noplanb.domain.user.domain;

import com.noplanb.domain.character.domain.Character;
import com.noplanb.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
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
    private String name;
    private String email;
    private String password;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", unique = true)
    private Character character;

}

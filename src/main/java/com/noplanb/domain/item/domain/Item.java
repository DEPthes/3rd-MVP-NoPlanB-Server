package com.noplanb.domain.item.domain;

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
public class Item extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String item_name;
    @Enumerated(EnumType.STRING)
    private ItemType itemType;
    private boolean  isEquipped;
    private Long requiredLevel;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id")
    private Character character;

    public void updateCharacter(Character character) {
        this.character = character;
    }
}

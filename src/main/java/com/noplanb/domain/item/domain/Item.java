package com.noplanb.domain.item.domain;

import com.noplanb.domain.character.domain.Character;
import com.noplanb.domain.common.BaseEntity;
import com.noplanb.domain.item_image.domain.ItemImage;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id")
    private Character character;

    private boolean  isEquipped;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_image_id")
    private ItemImage itemImage;

    @Builder
    public Item(Character character, boolean isEquipped, ItemImage itemImage) {
        this.character = character;
        this.isEquipped = isEquipped;
        this.itemImage = itemImage;
    }

    public void updateCharacter(Character character) {
        this.character = character;
    }

    public void updateEquipped(boolean b) {
        this.isEquipped = b;
    }
}

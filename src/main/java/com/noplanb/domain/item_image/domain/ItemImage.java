package com.noplanb.domain.item_image.domain;

import com.noplanb.domain.common.BaseEntity;
import com.noplanb.domain.item.domain.Item;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String itemImageUrl;

    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    public ItemImage(String itemImageUrl, Item item) {
        this.itemImageUrl = itemImageUrl;
        this.item = item;
    }

}

package com.noplanb.domain.item_image.domain;

import com.noplanb.domain.common.BaseEntity;
import com.noplanb.domain.item.domain.Item;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String itemImageUrl;

    private String itemName;

    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    private Long requiredLevel;

    @OneToMany(mappedBy = "itemImage")
    private List<Item> item = new ArrayList<>();


}

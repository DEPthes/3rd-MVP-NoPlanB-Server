package com.noplanb.domain.character.dto.response;

import com.noplanb.domain.item.domain.ItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MyCharaterDetailRes {
    private ItemType itemType;
    private String itemImage;
}

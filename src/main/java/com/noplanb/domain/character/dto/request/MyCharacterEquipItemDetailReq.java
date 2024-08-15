package com.noplanb.domain.character.dto.request;

import com.noplanb.domain.item_image.domain.ItemType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MyCharacterEquipItemDetailReq {
    @Schema(type = "string", example = "CLOTHES", description="아이템 타입")
    private ItemType itemType;
    @Schema(type = "long", example = "10", description="아이템 아이디")
    private Long itemId;
}

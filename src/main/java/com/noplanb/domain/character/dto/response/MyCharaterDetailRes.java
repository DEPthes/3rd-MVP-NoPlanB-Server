package com.noplanb.domain.character.dto.response;

import com.noplanb.domain.item.domain.ItemType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MyCharaterDetailRes {
    @Schema(type = "string", example = "HEAD", description = "아이템 타입")
    private ItemType itemType;
    @Schema(type = "string", example = "모자1.png", description = "아이템 이미지")
    private String itemImage;
}

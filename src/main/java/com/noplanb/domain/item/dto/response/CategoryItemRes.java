package com.noplanb.domain.item.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
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
public class CategoryItemRes {
    @Schema(type = "Long", example = "10", description = "아이템 ID")
    private Long itemId;

    @Schema(type = "String", example = "베레모", description = "아이템 이름")
    private String itemName;

    @Schema(type = "String", example = "베레모.png", description = "아이템 이미지")
    private String itemImage;

    @Schema(type = "String", example = "모자", description = "아이템 타입")
    private ItemType itemType;

    @Schema(type = "boolean", example = "false", description = "장착 가능여부 ( = 잠금해제여부)")
    private boolean ableToEquip;

    @Schema(type = "boolean", example = "false", description = "장착 여부")
    @JsonProperty("equipped")
    private boolean isEquipped;

    @Schema(type = "Long", example = "20", description = "필요 레벨")
    private Long requiredLevel;

}

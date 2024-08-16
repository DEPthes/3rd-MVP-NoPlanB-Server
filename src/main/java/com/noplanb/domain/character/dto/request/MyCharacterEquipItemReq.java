package com.noplanb.domain.character.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MyCharacterEquipItemReq {
    @Schema(type = "List", description="캐릭터 장착 아이템 리스트")
    private List<MyCharacterEquipItemDetailReq> myCharacterEquipItemDetailReqList;
}

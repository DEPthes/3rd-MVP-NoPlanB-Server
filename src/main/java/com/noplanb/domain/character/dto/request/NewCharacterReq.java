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
public class NewCharacterReq {

    @Schema(type = "string", example = "캐릭터 이름", description="캐릭터 이름")
    private String characterName;

    @Schema(type = "List", description="아이템 아이디 리스트")
    private List<Long> itemIdList;

}

package com.noplanb.domain.character.dto.response;

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
public class InitialCharacterInfoRes {

    @Schema(type = "String", example = "럭키조이", description = "캐릭터 이름")
    private String characterName;

    @Schema(type = "List", description = "캐릭터가 장착한 아이템 및 타입")
    private List<MyCharaterDetailRes> myCharaterDetailResList;
}

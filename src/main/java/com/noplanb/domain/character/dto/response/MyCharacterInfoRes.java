package com.noplanb.domain.character.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MyCharacterInfoRes {
    @Schema(type = "Long", example = "럭키조이", description = "캐릭터 ID")
    private String characterName;
    @Schema(type = "Long", example = "2024-08-08T17:50:19", description = "캐릭터 생성일")
    private LocalDateTime startDate;
    @Schema(type = "Long", example = "100", description = "획득한 총 exp")
    private Long totalExp;
    @Schema(type = "Long", example = "20", description = "달성한 총 퀘스트 수")
    private Long totalQuest;
    @Schema(type = "integer", example = "15", description = "성장일 수")
    private int growDate;
    @Schema(type = "List", description = "캐릭터가 장착한 아이템 리스트 및 타입")
    private List<MyCharaterDetailRes> myCharaterDetailResList;

}

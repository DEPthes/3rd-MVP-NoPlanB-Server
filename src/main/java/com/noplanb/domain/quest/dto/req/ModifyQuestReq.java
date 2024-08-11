package com.noplanb.domain.quest.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "ModifyQuestReq")
public class ModifyQuestReq {
    @Schema(type = "Long", example = "1", description = "수정할 퀘스트 ID")
    private Long id;
    @Schema(type = "String", example = "빨래하기", description = "퀘스트 내용")
    private String contents;
}

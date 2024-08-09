package com.noplanb.domain.quest.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
@Builder
@Getter
public class RetrieveCalendarRes {
    @Schema(type = "Long", example = "1", description = "퀘스트 아이디")
    private Long id;
    @Schema(type = "Long", example = "5", description = "퀘스트 경험치")
    private Long exp;
}

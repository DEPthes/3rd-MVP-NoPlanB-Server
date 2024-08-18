package com.noplanb.domain.quest.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RetrieveLevelAndTodayExpRes {
    @Schema(type = "Long", example = "11", description = "현재 레벨")
    private Long level;
    @Schema(type = "Long", example = "5", description = "다음 레벨업까지 획득한 경험치")
    private Long acquireExp;
    @Schema(type = "Long", example = "10", description = "다음 레벨업까지 필요한 경험치")
    private Long needExp;
    @Schema(type = "Long", example = "5", description = "오늘 얻은 경험치")
    private Long todayExp;
    @Schema(type = "Long", example = "3", description = "생성된 퀘스트들의 경험치 합")
    private Long totQuestExp;

}

package com.noplanb.domain.quest.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class RetrieveCalendarRes {
    @Schema(type = "LocalDate", example = "2024-08-03", description = "달력 날짜")
    private LocalDate date;
    @Schema(type = "Long", example = "5", description = "퀘스트 경험치")
    private Long exp;
}

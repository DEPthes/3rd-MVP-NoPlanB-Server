package com.noplanb.domain.quest.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class CreateQuestAfterTodayReq {
    @Schema(type = "LocalDate", example = "2024-08-29", description = "금일 기준 이후 날짜")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
    @Schema(type = "String", example = "청소하기", description = "퀘스트 내용")
    private String contents;
    @Schema(type = "Long", example = "5", description = "퀘스트 경험치")
    private Long exp;
}

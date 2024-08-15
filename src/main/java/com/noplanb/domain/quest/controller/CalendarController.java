package com.noplanb.domain.quest.controller;

import com.noplanb.domain.quest.application.CalendarService;
import com.noplanb.domain.quest.dto.req.CreateQuestAfterTodayReq;
import com.noplanb.domain.quest.dto.res.RetrieveCalendarRes;
import com.noplanb.global.config.security.token.CurrentUser;
import com.noplanb.global.config.security.token.UserPrincipal;
import com.noplanb.global.payload.ErrorResponse;
import com.noplanb.global.payload.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/calendar")
@Tag(name = "Calendar", description = "캘린더 관련 API")
public class CalendarController {
    private final CalendarService calendarService;
    @GetMapping("/{date}")
    @Operation(summary = "달력 경험치 조회", description = "달력경험치를 조회할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "달력 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "달력 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    public ResponseEntity<?> retrieveCalendar(
            @Parameter(example = "2024-08", description = "날짜를 입력해주세요", required = true) @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) YearMonth date,
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal){
        return calendarService.retrieveCalendar(date,userPrincipal);
    }
    @PostMapping
    @Operation(summary = "달력 금일기준 이후 퀘스트생성", description = "달력 금일기준 이후 퀘스트생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "달력 금일기준 이후 퀘스트생성 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RetrieveCalendarRes.class))}),
            @ApiResponse(responseCode = "400", description = "달력 금일기준 이후 퀘스트생성 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    public ResponseEntity<?> createQuestAfterToday(
            @Parameter(description = "달력 금일기준 이후 퀘스트생성 dto Req입니다.", required = true) @RequestBody CreateQuestAfterTodayReq createQuestAfterTodayReq,
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal){
        return calendarService.createQuestAfterToday(createQuestAfterTodayReq,userPrincipal);
    }
}

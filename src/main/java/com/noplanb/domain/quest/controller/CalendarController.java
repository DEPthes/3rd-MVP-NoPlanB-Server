package com.noplanb.domain.quest.controller;

import com.noplanb.domain.quest.application.CalendarService;
import com.noplanb.domain.quest.dto.res.RetrieveCalendarRes;
import com.noplanb.global.config.security.token.CurrentUser;
import com.noplanb.global.config.security.token.UserPrincipal;
import com.noplanb.global.payload.ErrorResponse;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.YearMonth;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/calendar")
@Tag(name = "CalendarController", description = "CalendarController입니다.")
public class CalendarController {
    private final CalendarService calendarService;
    @GetMapping("/{date}")
    @Operation(summary = "달력 경험치 조회", description = "달력경험치를 조회할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "달력 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RetrieveCalendarRes.class))}),
            @ApiResponse(responseCode = "400", description = "달력 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    public ResponseEntity<?> retrieveCalendar(
            @Parameter(example = "2024-08", description = "날짜를 입력해주세요", required = true) @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) YearMonth date,
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal){
        return calendarService.retrieveCalendar(date,userPrincipal);
    }
}

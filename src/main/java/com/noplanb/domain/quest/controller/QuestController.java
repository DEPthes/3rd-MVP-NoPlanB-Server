package com.noplanb.domain.quest.controller;

import com.noplanb.domain.quest.application.QuestService;
import com.noplanb.domain.quest.dto.req.CreateQuestReq;
import com.noplanb.domain.quest.dto.req.ModifyQuestReq;
import com.noplanb.domain.quest.dto.res.RetrieveLevelAndTodayExpRes;
import com.noplanb.domain.quest.dto.res.RetrieveQuestRes;
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

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/quest")
@Tag(name = "QuestController", description = "QuestController입니다.")
public class QuestController {
    private final QuestService questService;

    @PostMapping("/{id}")
    @Operation(summary = "퀘스트생성", description = "퀘스트를 생성할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "퀘스트 작성 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "퀘스트 작성 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    public ResponseEntity<?> createQuest(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @PathVariable Long id,
            @Parameter(description = "퀘스트생성 dto Req입니다.", required = true) @RequestBody CreateQuestReq createQuestReq){

        return questService.createQuest(createQuestReq, id);
    }
    @GetMapping("/{date}/{id}")
    @Operation(summary = "퀘스트조회", description = "퀘스트를 조회할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "퀘스트 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RetrieveQuestRes.class))}),
            @ApiResponse(responseCode = "400", description = "퀘스트 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    public ResponseEntity<?> retrieveQuest(
//            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(example = "2024-08-09", description = "날짜를 입력해주세요", required = true) @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @PathVariable Long id){
        System.out.println("date = " + date);
        return questService.retrieveQuest(date,id);
    }
    @GetMapping("/{id}")
    @Operation(summary = "메인페이지 상당부분", description = "메인페이지 상당부부을 조회할 떄 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메인페이지 상당부분 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RetrieveLevelAndTodayExpRes.class))}),
            @ApiResponse(responseCode = "400", description = "메인페이지 상당부분 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    public ResponseEntity<?> retrieveLevelAndTodayExp(
//        @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @PathVariable Long id) {
        return questService.retrieveLevelAndTodayExp(id);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "퀘스트 수정", description = "퀘스트 수정할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "퀘스트 수정 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "퀘스트 수정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    public ResponseEntity<?> modifyQuest(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @PathVariable Long id,
            @Parameter(description = "퀘스트수정 dto Req입니다.", required = true) @RequestBody ModifyQuestReq modifyQuestReq) {
        return questService.modifyQuest(id, modifyQuestReq);
    }
    @DeleteMapping("/{userId}/{id}")
    @Operation(summary = "퀘스트 삭제", description = "퀘스트 삭제할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "퀘스트 삭제 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "퀘스트 삭제 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    public ResponseEntity<?> deleteQuest(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @PathVariable Long userId,
            @Parameter(description = "삭제할 퀘스트 아이디를 입력해주세요.", required = true) @PathVariable Long id){
        return questService.deleteQuest(userId,id);
    }

}
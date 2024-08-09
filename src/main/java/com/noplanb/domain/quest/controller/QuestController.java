package com.noplanb.domain.quest.controller;

import com.noplanb.domain.quest.application.QuestService;
import com.noplanb.domain.quest.dto.req.CreateQuestReq;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "QuestController", description = "QuestController입니다.")
public class QuestController {
    private final QuestService questService;

    @PostMapping("/{id}")
    @Operation(summary = "퀘스트생성", description = "퀘스트를 생성할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 작성 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "댓글 작성 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    public ResponseEntity<?> createQuest(
//            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @PathVariable Long id,
            @Parameter(description = "Schemas의 CreateQuestReq를 참고해주세요", required = true) @RequestBody CreateQuestReq createQuestReq){
        return questService.createQuest(createQuestReq, id);
    }
    @GetMapping("")
    @Operation(summary = "퀘스트조회", description = "퀘스트를 조회할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "퀘스트 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RetrieveQuestRes.class))}),
            @ApiResponse(responseCode = "400", description = "퀘스트 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    public ResponseEntity<?> retrieveQuest(
//            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @PathVariable Long id){
        return questService.retrieveQuest(id);
    }
    @GetMapping("/{id}")
    @Operation(summary = "메인페이지 상당부분", description = "메인페이지 상당부부을 조회할 떄 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "퀘스트 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RetrieveLevelAndTodayExpRes.class))}),
            @ApiResponse(responseCode = "400", description = "퀘스트 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    public ResponseEntity<?> retrieveLevelAndTodayExp(
//            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @PathVariable Long id){
        return questService.retrieveLevelAndTodayExp(id);
    }
    @GetMapping("kj")
    @Operation(summary = "캐릭터조회", description = "메인페이지에 캐릭터를 가져올 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "퀘스트 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RetrieveQuestRes.class))}),
            @ApiResponse(responseCode = "400", description = "퀘스트 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    public ResponseEntity<?> retrieveCharacter(
//            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @PathVariable Long id){
        return questService.retrieveCharacter(id);
    }

}
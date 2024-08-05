package com.noplanb.domain.quest.controller;

import com.noplanb.domain.quest.application.QuestService;
import com.noplanb.domain.quest.dto.req.CreateQuestReq;
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

}
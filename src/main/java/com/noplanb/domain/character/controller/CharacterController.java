package com.noplanb.domain.character.controller;

import com.noplanb.domain.character.application.CharacterService;
import com.noplanb.domain.character.dto.request.UpdateNameReq;
import com.noplanb.domain.character.dto.response.MyCharacterInfoRes;
import com.noplanb.domain.character.dto.response.MyCharaterListRes;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/character")
@Tag(name = "CharacterController", description = "CharacterController입니다.")
public class CharacterController {

    private final CharacterService characterService;


    @Operation(summary = "캐릭터 조회 API", description = "캐릭터 장착 아이템 이미지를 보여주는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "캐릭터 조회 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = MyCharaterListRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "캐릭터 조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping("/")
    public ResponseEntity<?> getMyCharacter(
            @Parameter @CurrentUser UserPrincipal userPrincipal
    ){
        return characterService.getMyCharacter(userPrincipal);
    }

    @Operation(summary = "캐릭터와 정보 조회 API", description = "마이페이지에서 캐릭터와 캐릭터 정보를 보여주는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "캐릭터 정보 조회 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = MyCharacterInfoRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "캐릭터 정보 조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping("/my")
    public ResponseEntity<?> getMyCharacterInfo(@Parameter @CurrentUser UserPrincipal userPrincipal){
        return characterService.getMyCharacterInfo(userPrincipal);
    }

    @Operation(summary = "캐릭터와 이름 업데이터 API", description = "마이페이지에서 캐릭터 이름을 변경하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "캐릭터 이름 업데이트 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
            @ApiResponse(responseCode = "400", description = "캐릭터 이름 업데이트 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping("/my")
    public ResponseEntity<?> getMyCharacterInfo(@Parameter @CurrentUser UserPrincipal userPrincipal, @RequestBody UpdateNameReq updateNameReq){
        return characterService.updateCharacterName(userPrincipal, updateNameReq);
    }



}

package com.noplanb.domain.item.controller;

import com.noplanb.domain.item.dto.response.CategoryItemListRes;
import com.noplanb.domain.item.application.ItemService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
@Tag(name = "ItemController", description = "ItemControllerController입니다.")
public class ItemController {

    private final ItemService itemService;

    @Operation(summary = "헤어 카테고리 아이템 조회 API", description = "헤어 카테고리에 해당하는 아이템 목록을 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "헤어 아이템 목록 조회 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryItemListRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "헤어 아이템 목록 조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping("/hair")
    public ResponseEntity<?> getHairItemList(@Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal){
        return itemService.getHairItemList(userPrincipal);
    }


    @Operation(summary = "얼굴 카테고리 아이템 조회 API", description = "얼굴 카테고리에 해당하는 아이템 목록을 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "얼굴 아이템 목록 조회 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryItemListRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "얼굴 아이템 목록 조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping("/face")
    public ResponseEntity<?> getFaceItemList(@Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal){
        return itemService.getFaceItemList(userPrincipal);
    }



    @Operation(summary = "패션 카테고리 아이템 조회 API", description = "패션 카테고리에 해당하는 아이템 목록을 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "패션 아이템 목록 조회 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryItemListRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "패션 아이템 목록 조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping("/fashion")
    public ResponseEntity<?> getFashionItemList(@Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal){
        return itemService.getFashionItemList(userPrincipal);
    }

    @Operation(summary = "배경 카테고리 아이템 조회 API", description = "배경 카테고리에 해당하는 아이템 목록을 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "배경 아이템 목록 조회 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryItemListRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "배경 아이템 목록 조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping("/background")
    public ResponseEntity<?> getBackgroundItemList(@Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal){
        return itemService.getBackgroundItemList(userPrincipal);
    }

}

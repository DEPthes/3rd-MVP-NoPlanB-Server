package com.noplanb.domain.item.controller;

import com.noplanb.domain.item.application.ItemService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
@Tag(name = "Item", description = "아이템 관련 API")
public class ItemController {

    private final ItemService itemService;

    @Operation(summary = "헤어 카테고리 아이템 조회 API", description = "헤어 카테고리에 해당하는 아이템 목록을 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "헤어 아이템 목록 조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping("/hair/{userId}")
    public ResponseEntity<?> getHairItemList(@Parameter(description = "Access Token을 입력해주세요.", required = true) @PathVariable Long userId){
        return ResponseEntity.ok(createApiResponse(itemService.getHairItemList(userId)));
    }


    @Operation(summary = "얼굴 카테고리 아이템 조회 API", description = "얼굴 카테고리에 해당하는 아이템 목록을 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "얼굴 아이템 목록 조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping("/cache/face/{userId}")
    public ResponseEntity<?> getcacheFaceItemList(@Parameter(description = "Access Token을 입력해주세요.", required = true) @PathVariable Long userId){
        return ResponseEntity.ok(createApiResponse(itemService.getcacheFaceItemList(userId)));
    }
    @GetMapping("/nocache/face/{userId}")
    public ResponseEntity<?> getnocacheFaceItemList(@Parameter(description = "Access Token을 입력해주세요.", required = true) @PathVariable Long userId){
        return ResponseEntity.ok(createApiResponse(itemService.getnocacheFaceItemList(userId)));
    }



    @Operation(summary = "패션 카테고리 아이템 조회 API", description = "패션 카테고리에 해당하는 아이템 목록을 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "패션 아이템 목록 조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping("/fashion/{userId}")
    public ResponseEntity<?> getFashionItemList(@Parameter(description = "Access Token을 입력해주세요.", required = true) @PathVariable Long userId){
        return ResponseEntity.ok(createApiResponse(itemService.getFashionItemList(userId)));
    }

    @Operation(summary = "배경 카테고리 아이템 조회 API", description = "배경 카테고리에 해당하는 아이템 목록을 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "배경 아이템 목록 조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping("/background/{userId}")
    public ResponseEntity<?> getBackgroundItemList(@Parameter(description = "Access Token을 입력해주세요.", required = true) @PathVariable Long userId){
        return ResponseEntity.ok(createApiResponse(itemService.getBackgroundItemList(userId)));
    }
    private <T> ResponseEntity<com.noplanb.global.payload.ApiResponse> createApiResponse(T information) {
        com.noplanb.global.payload.ApiResponse apiResponse = com.noplanb.global.payload.ApiResponse.builder()
                .check(true)
                .information(information)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

}

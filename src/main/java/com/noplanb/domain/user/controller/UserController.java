package com.noplanb.domain.user.controller;

import com.amazonaws.Response;
import com.noplanb.domain.user.application.UserService;
import com.noplanb.domain.user.dto.response.MyAccountRes;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "User", description = "사용자 관련 API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "연결된 계정 조회 API", description = "마이페이지에서 로그인 시 연결된 계정 정보를 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "계정 조회 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = MyAccountRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "계정 조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping("/account")
    public ResponseEntity<?> getMyAccount(@Parameter @CurrentUser UserPrincipal userPrincipal){
        return userService.getMyAccount(userPrincipal);
    }

}

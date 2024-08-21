package com.noplanb.domain.auth.controller;

import com.noplanb.domain.auth.application.AuthService;
import com.noplanb.domain.auth.dto.request.SigninReq;
import com.noplanb.domain.auth.dto.response.LoginResponse;
import com.noplanb.domain.character.dto.response.MyCharaterListRes;
import com.noplanb.global.config.security.token.CurrentUser;
import com.noplanb.global.config.security.token.UserPrincipal;
import com.noplanb.global.payload.ErrorCode;
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
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "로그인 관련 API")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "카카오 로그인 API", description = "카카오 로그인 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 및 토큰 발급 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation =LoginResponse.class) ) } ),
            @ApiResponse(responseCode = "400", description = "로그인 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping("/idTokenLogin")
    public ResponseEntity<?> idTokenLogin(@RequestBody SigninReq signinReq) {
        String accessToken = signinReq.getAccessToken();
        String email = signinReq.getEmail();

        if (accessToken == null || accessToken.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("ID 토큰은 null이거나 비어 있을 수 없습니다");
        }
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("이메일은 null이거나 비어 있을 수 없습니다");
        }

        try {
            return authService.loginWithIdToken(accessToken, email);
        } catch (RuntimeException e) {
            ErrorCode errorCode = ErrorCode.INVALID_TOKEN;
            ErrorResponse errorResponse = ErrorResponse.of(errorCode, e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Operation(summary = "로그아웃 API", description = "로그아웃 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
            @ApiResponse(responseCode = "400", description = "로그아웃 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Parameter @CurrentUser UserPrincipal userPrincipal) {
        return authService.logout(userPrincipal);
    }

}
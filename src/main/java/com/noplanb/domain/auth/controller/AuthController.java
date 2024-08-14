package com.noplanb.domain.auth.controller;

import com.noplanb.domain.auth.application.AuthService;
import com.noplanb.domain.auth.dto.request.SigninReq;
import com.noplanb.domain.auth.dto.response.LoginResponse;
import com.noplanb.global.payload.ApiResponse;
import com.noplanb.global.payload.ErrorCode;
import com.noplanb.global.payload.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

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
}
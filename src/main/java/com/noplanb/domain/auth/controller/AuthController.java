package com.noplanb.domain.auth.controller;

import com.noplanb.domain.auth.application.AuthService;
import com.noplanb.domain.auth.dto.request.SigninReq;
import com.noplanb.domain.auth.dto.response.LoginResponse;
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
            LoginResponse loginResponse = authService.loginWithIdToken(accessToken, email);
            Map<String, String> response = new HashMap<>();
            response.put("accessToken", loginResponse.getAccessToken());
            response.put("refreshToken", loginResponse.getRefreshToken());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
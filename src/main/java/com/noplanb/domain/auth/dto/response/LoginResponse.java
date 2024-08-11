package com.noplanb.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoginResponse {
    @Schema(type = "String", example = "eyJhbGciOidfqnkd", description = "JWT Access Token")
    private String accessToken;
    @Schema(type = "String", example = "eyJhbGciweifnsad", description = "JWT Refresh Token")
    private String refreshToken;
}

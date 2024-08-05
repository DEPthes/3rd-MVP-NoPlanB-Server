package com.noplanb.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SignInReq {
    @Schema(type = "string", example = "string@aa.bb", description="계정 이메일 입니다.")
    @Email
    @NotNull(message = "이메일은 필수 입력 값입니다.")
    //@JsonProperty("email")
    private String email;

    @Schema(type = "string", example = "access_token_string", description="사용자의 Access Token 입니다.")
    @NotNull(message = "Access Token은 필수 입력 값입니다.")
    private String accessToken;

}
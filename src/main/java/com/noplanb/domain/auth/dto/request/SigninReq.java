package com.noplanb.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SigninReq {
    @Schema(type = "string", example = "accessToken_string", description="사용자의 ID Token 입니다.")
    private String accessToken;
    @Schema(type = "string", example = "string@aa.bb", description="계정 이메일 입니다.")
    private String email;
}
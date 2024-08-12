package com.noplanb.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MyAccountRes {
    @Schema(type = "String", example = "hbj0209@kakao.com", description = "서비스에 연결된 유저의 카카오 로그인 이메일")
    private String email;
}

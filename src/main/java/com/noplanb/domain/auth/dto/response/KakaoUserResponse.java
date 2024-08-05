package com.noplanb.domain.auth.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KakaoUserResponse {
    private Long id;
    private KakaoAccount kakaoAccount;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class KakaoAccount {
        private String email;
        private String nickname;
    }
}

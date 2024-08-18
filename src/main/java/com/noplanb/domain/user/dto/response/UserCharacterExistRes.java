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
public class UserCharacterExistRes {
    @Schema(type = "boolean", example = "true", description = "유저의 캐릭터 생성 여부")
    private boolean isExist;
}

package com.noplanb.domain.character.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateNameReq {
    @Schema(type = "string", example = "럭키조이", description="변경할 캐릭터 이름")
    private String newCharacterName;
}

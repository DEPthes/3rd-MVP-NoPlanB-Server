package com.noplanb.domain.quest.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RetrieveLevelUpItemImage {
    @Schema(type = "String", example = "uuid", description = "이미지 url")
    private String itemImageUrl;
}

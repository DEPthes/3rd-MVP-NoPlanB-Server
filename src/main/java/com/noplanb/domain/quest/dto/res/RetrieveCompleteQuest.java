package com.noplanb.domain.quest.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RetrieveCompleteQuest {
    @Schema(type = "String", example = "완료", description = "완료한 퀘스트의 상태")
    private String questType;
    @Schema(type = "List", example = "{\n" +
            "    \"check\": true,\n" +
            "    \"information\": {\n" +
            "        \"questType\": \"완료\",\n" +
            "        \"itemImageUrls\": []\n" +
            "    }\n" +
            "}", description = "이미지 url")
    private List<String> itemImageUrls;
}

package com.noplanb.domain.quest.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RetrieveCompleteQuest {
    @Schema(type = "String", example = "해금", description = "완료한 퀘스트의 상태")
    private String questType;
    @Schema(type = "List", example = "[\n" +
            "            \"https://growme-s3-image.s3.ap-northeast-2.amazonaws.com/e239c87f-0022-4ddc-9411-12f753a12bae8.png\",\n" +
            "            \"https://growme-s3-image.s3.ap-northeast-2.amazonaws.com/ea8c8aad-38ae-4359-9f5e-6e5213ab74f99.png\",\n" +
            "            \"https://growme-s3-image.s3.ap-northeast-2.amazonaws.com/d5ac7939-51a6-48b9-ba21-63197335c52c15.png\",\n" +
            "            \"https://growme-s3-image.s3.ap-northeast-2.amazonaws.com/15a2aa11-6324-4471-a90e-b8d5f7e235b316.png\",\n" +
            "            \"https://growme-s3-image.s3.ap-northeast-2.amazonaws.com/3936732a-433f-4008-a572-0f2fb42e2b9517.png\"\n" +
            "        ]", description = "이미지 url")
    private List<String> itemImageUrls;
}

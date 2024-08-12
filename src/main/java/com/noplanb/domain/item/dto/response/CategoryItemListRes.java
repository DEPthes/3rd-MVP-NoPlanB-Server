package com.noplanb.domain.item.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CategoryItemListRes {
    @Schema(type = "List", description = "카테고리 내 아이템 정보들을 담은 리스트")
    List<CategoryItemRes> categoryItemList;
}

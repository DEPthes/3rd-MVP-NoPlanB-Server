package com.noplanb.domain.character.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MyCharaterListRes {
    private List<MyCharaterDetailRes> myCharaterDetailResList;
}

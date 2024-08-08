package com.noplanb.domain.character.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MyCharacterInfoRes {
    private String characterName;
    private LocalDateTime startDate;
    private Long totalExp;
    private Long totalQuest;
    private int growDate;
    private List<MyCharaterDetailRes> myCharaterDetailResList;

}

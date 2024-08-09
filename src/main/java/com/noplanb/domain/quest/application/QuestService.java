package com.noplanb.domain.quest.application;

import com.noplanb.domain.character.domain.Character;
import com.noplanb.domain.character.repository.CharacterRepository;
import com.noplanb.domain.quest.domain.Quest;
import com.noplanb.domain.quest.dto.req.CreateQuestReq;
import com.noplanb.domain.quest.dto.res.RetrieveLevelAndTodayExpRes;
import com.noplanb.domain.quest.dto.res.RetrieveQuestRes;
import com.noplanb.domain.quest.repository.QuestRepository;
import com.noplanb.global.payload.ApiResponse;
import com.noplanb.global.payload.Message;
import com.noplanb.global.payload.exception.CharacterNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.noplanb.global.payload.ErrorCode.CHARACTER_NOT_FOUND;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestService {
    private final CharacterRepository characterRepository;
    private final QuestRepository questRepository;

    @Transactional
    public ResponseEntity<?> createQuest(CreateQuestReq createQuestReq, Long id) {
        Character character = characterRepository.findById(id).orElseThrow(CharacterNotFoundException::new);

        Quest quest = Quest.builder()
                .contents(createQuestReq.getContents())
                .exp(createQuestReq.getExp())
                .character(character)
                .isComplete(Boolean.FALSE)
                .build();

        questRepository.save(quest);
        //양방향 연관관계
        character.getQuests().add(quest);

        return createApiResponse((Message.builder().message("퀘스트를 만들었습니다.").build()));
    }
    public ResponseEntity<?> retrieveQuest(Long id) {
        Character character = characterRepository.findById(id).orElseThrow(CharacterNotFoundException::new);
        List<Quest> quests = character.getQuests();

        List<RetrieveQuestRes> retrieveQuestResList = quests.stream()
                .map(quest -> RetrieveQuestRes.builder()
                        .contents(quest.getContents())
                        .exp(quest.getExp())
                        .build())
                .collect(Collectors.toList());

        return createApiResponse(retrieveQuestResList);
    }

    public ResponseEntity<?> retrieveLevelAndTodayExp(Long id) {
        Character character = characterRepository.findById(id).orElseThrow(CharacterNotFoundException::new);

        Long nextLevelNeedExp = (((character.getLevel()+1)*(character.getLevel()+2))*10)/2;
        Long totalExp = character.getTotalExp();

        RetrieveLevelAndTodayExpRes retrieveLevelAndTodayExpRes = RetrieveLevelAndTodayExpRes.builder()
                .level(character.getLevel())
                .gauge(nextLevelNeedExp - totalExp)
                .todayExp(character.getTodayExp())
                .build();

        return createApiResponse(retrieveLevelAndTodayExpRes);
    }

    public ResponseEntity<?> retrieveCharacter(Long id) {
        // 추후 구현 예정
        return null;
    }
    private <T> ResponseEntity<ApiResponse> createApiResponse(T information) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(information)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}

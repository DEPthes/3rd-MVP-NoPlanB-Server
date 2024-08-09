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

import java.time.LocalDate;
import java.util.ArrayList;
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
    public ResponseEntity<?> retrieveQuest(LocalDate localDate, Long id) {
        Character character = characterRepository.findById(id).orElseThrow(CharacterNotFoundException::new);
        List<Quest> quests = character.getQuests();
        // 퀘스트 날짜 가져오기
        List<Quest> dateQuests = quests.stream().filter(quest -> quest.getCreatedAt().toLocalDate().isEqual(localDate))
                .collect(Collectors.toList());
        // 퀘스트 미완료
        List<RetrieveQuestRes> incompleteQuests = dateQuests.stream()
                .filter(quest -> !quest.getIsComplete())
                .map(quest -> RetrieveQuestRes.builder()
                        .id(quest.getId())
                        .contents(quest.getContents())
                        .exp(quest.getExp())
                        .build())
                .collect(Collectors.toList());
        // 퀘스트 완료
        List<RetrieveQuestRes> completeQuests = dateQuests.stream()
                .filter(quest -> quest.getIsComplete())
                .map(quest -> RetrieveQuestRes.builder()
                        .id(quest.getId())
                        .contents(quest.getContents())
                        .exp(quest.getExp())
                        .build())
                .collect(Collectors.toList());

        //미완료 완료 합치기
        List<RetrieveQuestRes> sortedQuests = new ArrayList<>();
        sortedQuests.addAll(incompleteQuests);
        sortedQuests.addAll(completeQuests);

        return createApiResponse(sortedQuests);
    }

    public ResponseEntity<?> retrieveLevelAndTodayExp(Long id) {
        Character character = characterRepository.findById(id).orElseThrow(CharacterNotFoundException::new);
        Long level = character.getLevel();
        System.out.println(character.getTotalExp());
        Long acquireExp = character.getTotalExp() - (((level-1)*level)/2)*10;

        RetrieveLevelAndTodayExpRes retrieveLevelAndTodayExpRes = RetrieveLevelAndTodayExpRes.builder()
                .level(level)
                .acquireExp(acquireExp)
                .needExp(level*10)
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

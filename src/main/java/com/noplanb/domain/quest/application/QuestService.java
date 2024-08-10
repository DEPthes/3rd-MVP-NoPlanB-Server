package com.noplanb.domain.quest.application;

import com.noplanb.domain.character.domain.Character;
import com.noplanb.domain.character.repository.CharacterRepository;
import com.noplanb.domain.quest.domain.Quest;
import com.noplanb.domain.quest.dto.req.CreateQuestReq;
import com.noplanb.domain.quest.dto.req.ModifyQuestReq;
import com.noplanb.domain.quest.dto.res.RetrieveLevelAndTodayExpRes;
import com.noplanb.domain.quest.dto.res.RetrieveQuestRes;
import com.noplanb.domain.quest.repository.QuestRepository;
import com.noplanb.global.payload.ApiResponse;
import com.noplanb.global.payload.Message;
import com.noplanb.global.payload.exception.CharacterNotFoundException;
import com.noplanb.global.payload.exception.QuestNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        // 특정 날짜에 해당하는 퀘스트 필터링 후 미완료 완료 로 정렬 후 생성순으로 정렬
        List<RetrieveQuestRes> retrieveQuestResList = quests.stream()
                .filter(quest -> quest.getCreatedAt().toLocalDate().isEqual(localDate))
                .sorted(Comparator.comparing(Quest::getIsComplete)
                        .thenComparing(Quest::getCreatedAt))
                .map(quest -> RetrieveQuestRes.builder()
                        .id(quest.getId())
                        .contents(quest.getContents())
                        .exp(quest.getExp())
                        .isComplete(quest.getIsComplete())
                        .build())
                .collect(Collectors.toList());

        return createApiResponse(retrieveQuestResList);
    }

    public ResponseEntity<?> retrieveLevelAndTodayExp(Long id) {
        Character character = characterRepository.findById(id).orElseThrow(CharacterNotFoundException::new);
        Long level = character.getLevel();
        Long acquireExp = character.getTotalExp() - (((level-1)*level)/2)*10;

        RetrieveLevelAndTodayExpRes retrieveLevelAndTodayExpRes = RetrieveLevelAndTodayExpRes.builder()
                .level(level)
                .acquireExp(acquireExp)
                .needExp(level*10)
                .todayExp(character.getTodayExp())
                .build();

        return createApiResponse(retrieveLevelAndTodayExpRes);
    }
    @Transactional
    public ResponseEntity<?> modifyQuest(Long id, ModifyQuestReq modifyQuestReq) {
        Character character = characterRepository.findById(id).orElseThrow(CharacterNotFoundException::new);
        List<Quest> quests = character.getQuests();
        // 퀘스트 날짜 가져오기
        Quest quest = quests.stream().filter(q -> q.getId().equals(modifyQuestReq.getId()))
                .findFirst()
                .orElseThrow(QuestNotFoundException::new);

        quest.updateContents(modifyQuestReq.getContents());
        return createApiResponse(Message.builder().message("퀘스트를 수정했습니다.").build());
    }
    @Transactional
    public ResponseEntity<?> deleteQuest(Long userId,Long id) {
        Character character = characterRepository.findById(userId).orElseThrow(CharacterNotFoundException::new);
        List<Quest> quests = character.getQuests();

        Quest quest = quests.stream().filter(q -> q.getId().equals(id))
                .findFirst()
                .orElseThrow(QuestNotFoundException::new);
        questRepository.delete(quest);

        return createApiResponse(Message.builder().message("퀘스트를 삭제했습니다.").build());
    }
    private <T> ResponseEntity<ApiResponse> createApiResponse(T information) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(information)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}

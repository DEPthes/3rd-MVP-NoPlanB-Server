package com.noplanb.domain.quest.application;

import com.noplanb.domain.character.domain.Character;
import com.noplanb.domain.character.repository.CharacterRepository;
import com.noplanb.domain.item.domain.Item;
import com.noplanb.domain.item_image.domain.ItemImage;
import com.noplanb.domain.item_image.domain.repository.ItemImageRepository;
import com.noplanb.domain.quest.domain.DailyExperience;
import com.noplanb.domain.quest.domain.Quest;
import com.noplanb.domain.quest.dto.req.CreateQuestReq;
import com.noplanb.domain.quest.dto.req.ModifyQuestReq;
import com.noplanb.domain.quest.dto.res.RetrieveLevelAndTodayExpRes;
import com.noplanb.domain.quest.dto.res.RetrieveLevelUpItemImage;
import com.noplanb.domain.quest.dto.res.RetrieveQuestRes;
import com.noplanb.domain.quest.repository.DailyExperienceRepository;
import com.noplanb.domain.quest.repository.QuestRepository;
import com.noplanb.global.config.security.token.UserPrincipal;
import com.noplanb.global.payload.ApiResponse;
import com.noplanb.global.payload.Message;
import com.noplanb.global.payload.exception.CharacterNotFoundException;
import com.noplanb.global.payload.exception.QuestNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestService {
    private final CharacterRepository characterRepository;
    private final QuestRepository questRepository;
    private final DailyExperienceRepository dailyExperienceRepository;
    private final ItemImageRepository itemImageRepository;

    @Transactional
    public ResponseEntity<?> createQuest(CreateQuestReq createQuestReq, UserPrincipal userPrincipal) {
        Character character = characterRepository.findById(userPrincipal.getId()).orElseThrow(CharacterNotFoundException::new);

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
    public ResponseEntity<?> retrieveQuest(LocalDate localDate, UserPrincipal userPrincipal) {
        Character character = characterRepository.findById(userPrincipal.getId()).orElseThrow(CharacterNotFoundException::new);
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

    public ResponseEntity<?> retrieveLevelAndTodayExp(UserPrincipal userPrincipal) {
        Character character = characterRepository.findById(userPrincipal.getId()).orElseThrow(CharacterNotFoundException::new);
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
    public ResponseEntity<?> completeQuest(UserPrincipal userPrincipal, Long id) {
        Character character = characterRepository.findById(userPrincipal.getId()).orElseThrow(CharacterNotFoundException::new);
        Long characterLevel = character.getLevel();
        List<Quest> quests = character.getQuests();
        // 퀘스트 가져오기
        Quest quest = quests.stream().filter(q -> q.getId().equals(id))
                .findFirst()
                .orElseThrow(QuestNotFoundException::new);

        //퀘스트 완료처리
        quest.updateIsComplete(Boolean.TRUE);
        //오늘얻은 경험치 update
        character.updateExp(quest.getExp());

        // 레벨업 확인
        if (character.getTotalExp()>((characterLevel*(characterLevel+1)/2)*10)){
            // 레벨업
            character.updateLevel();
            // 레벌업된 레벨이 해금대상인지 확인
            if (character.getLevel() % 5 == 0) {
                //아이템 해금
                List<Item> unLockItems = unLockItem(character.getLevel(), character);
                //해금된 아이템들 이미지 반환
                List<RetrieveLevelUpItemImage> unLockImages = unLockItems.stream()
                        .map(item -> RetrieveLevelUpItemImage.builder()
                                .itemImageUrl(itemImageRepository.findItemImageByItem(item).getItemImageUrl())
                                .build())
                        .collect(Collectors.toList());
                return createApiResponse(unLockImages);
            }
            return createApiResponse(Message.builder().message("레벨업을 했습니다!").build());
        }
        return createApiResponse(Message.builder().message("퀘스트를 완료했습니다!").build());
    }

    private List<Item> unLockItem(Long level, Character character) {
        List<Item> items = character.getItems();
        List<Item> lockItems = items.stream().filter(item -> item.getRequiredLevel().equals(level))
                .collect(Collectors.toList());
        return lockItems;

    }

    @Transactional
    public ResponseEntity<?> modifyQuest(UserPrincipal userPrincipal, ModifyQuestReq modifyQuestReq) {
        Character character = characterRepository.findById(userPrincipal.getId()).orElseThrow(CharacterNotFoundException::new);
        List<Quest> quests = character.getQuests();
        // 퀘스트 가져오기
        Quest quest = quests.stream().filter(q -> q.getId().equals(modifyQuestReq.getId()))
                .findFirst()
                .orElseThrow(QuestNotFoundException::new);

        quest.updateContents(modifyQuestReq.getContents());
        return createApiResponse(Message.builder().message("퀘스트를 수정했습니다.").build());
    }
    @Transactional
    public ResponseEntity<?> deleteQuest(UserPrincipal userPrincipal, Long id) {
        Character character = characterRepository.findById(userPrincipal.getId()).orElseThrow(CharacterNotFoundException::new);
        List<Quest> quests = character.getQuests();

        Quest quest = quests.stream().filter(q -> q.getId().equals(id))
                .findFirst()
                .orElseThrow(QuestNotFoundException::new);
        questRepository.delete(quest);

        return createApiResponse(Message.builder().message("퀘스트를 삭제했습니다.").build());
    }
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void resetDailyExperience() {
        List<Character> characters = characterRepository.findAll();
        for (Character character : characters) {
            // 어제 날짜로 DailyExperience 엔티티에 저장
            LocalDate yesterday = LocalDate.now().minusDays(1);
            DailyExperience dailyExperience = new DailyExperience(character.getId(), yesterday, character.getTodayExp());
            dailyExperienceRepository.save(dailyExperience);

            // 사용자 경험치 초기화
            character.resetTodayExp();
        }
    }

    private <T> ResponseEntity<ApiResponse> createApiResponse(T information) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(information)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}

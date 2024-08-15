package com.noplanb.domain.quest.application;

import com.noplanb.domain.character.domain.Character;
import com.noplanb.domain.character.repository.CharacterRepository;
import com.noplanb.domain.quest.domain.DailyExperience;
import com.noplanb.domain.quest.domain.Quest;
import com.noplanb.domain.quest.dto.req.CreateQuestAfterTodayReq;
import com.noplanb.domain.quest.dto.res.RetrieveCalendarRes;
import com.noplanb.domain.quest.repository.DailyExperienceRepository;
import com.noplanb.domain.quest.repository.QuestRepository;
import com.noplanb.global.config.security.token.UserPrincipal;
import com.noplanb.global.payload.ApiResponse;
import com.noplanb.global.payload.Message;
import com.noplanb.global.payload.exception.CharacterNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarService {
    private final CharacterRepository characterRepository;
    private final DailyExperienceRepository dailyExperienceRepository;
    private final QuestRepository questRepository;

    public ResponseEntity<?> retrieveCalendar(YearMonth date, UserPrincipal userPrincipal) {
        LocalDate startDate = date.atDay(1);
        LocalDate endDate = date.atEndOfMonth();

        //그 달의 첫날부터 마지막날까지의 경험치들 가져오기
        List<DailyExperience> calendarList = dailyExperienceRepository.findByCharacterIdAndDateBetweenOrderByDateAsc(userPrincipal.getId(), startDate, endDate);

        List<RetrieveCalendarRes> calendarRes = calendarList.stream().
                map(calendar -> RetrieveCalendarRes.builder()
                        .date(calendar.getDate())
                        .exp(calendar.getTodayExp())
                        .build())
                .collect(Collectors.toList());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(calendarRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
    @Transactional
    public ResponseEntity<?> createQuestAfterToday(CreateQuestAfterTodayReq createQuestAfterTodayReq, UserPrincipal userPrincipal) {
        Character character = characterRepository.findByUserId(userPrincipal.getId()).orElseThrow(CharacterNotFoundException::new);
        //미리 추가하는 경우는 자정으로 설정한다.
        LocalDateTime dateTimeAtMidnight = createQuestAfterTodayReq.getDate().atStartOfDay();
        Quest quest = Quest.builder()
                .createdTime(dateTimeAtMidnight)
                .contents(createQuestAfterTodayReq.getContents())
                .exp(createQuestAfterTodayReq.getExp())
                .character(character)
                .isComplete(Boolean.FALSE)
                .build();

        questRepository.save(quest);
        character.getQuests().add(quest);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information((Message.builder().message("금일 기준 달력 퀘스트를 만들었습니다.").build()))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}

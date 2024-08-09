package com.noplanb.domain.quest.application;

import com.noplanb.domain.character.domain.Character;
import com.noplanb.domain.character.repository.CharacterRepository;
import com.noplanb.domain.quest.domain.Quest;
import com.noplanb.domain.quest.dto.res.RetrieveCalendarRes;
import com.noplanb.global.payload.ApiResponse;
import com.noplanb.global.payload.exception.CharacterNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarService {
    private final CharacterRepository characterRepository;

    public ResponseEntity<?> retrieveQuest(YearMonth date, Long id) {
        Character character = characterRepository.findById(id).orElseThrow(CharacterNotFoundException::new);
        List<Quest> quests = character.getQuests();
        // YearMonth 필터링
        List<Quest> filteredByDate = quests.stream()
                .filter(quest -> YearMonth.from(quest.getCreatedAt().toLocalDate()).equals(date))
                .collect(Collectors.toList());

        List<RetrieveCalendarRes> calendarRes = filteredByDate.stream().
                map(f -> RetrieveCalendarRes.builder()
                        .id(f.getId())
                        .exp(f.getExp())
                        .build())
                .collect(Collectors.toList());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(calendarRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}

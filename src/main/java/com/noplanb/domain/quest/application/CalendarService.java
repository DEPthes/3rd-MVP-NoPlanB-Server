package com.noplanb.domain.quest.application;

import com.noplanb.domain.character.domain.Character;
import com.noplanb.domain.character.repository.CharacterRepository;
import com.noplanb.domain.quest.domain.DailyExperience;
import com.noplanb.domain.quest.domain.Quest;
import com.noplanb.domain.quest.dto.res.RetrieveCalendarRes;
import com.noplanb.domain.quest.repository.DailyExperienceRepository;
import com.noplanb.global.payload.ApiResponse;
import com.noplanb.global.payload.exception.CharacterNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    public ResponseEntity<?> retrieveCalendar(YearMonth date, Long id) {
        LocalDate startDate = date.atDay(1);
        LocalDate endDate = date.atEndOfMonth();

        //그 달의 첫날부터 마지막날까지의 경험치들 가져오기
        List<DailyExperience> calendarList = dailyExperienceRepository.findByCharacterIdAndDateBetweenOrderByDateAsc(id, startDate, endDate);

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
}

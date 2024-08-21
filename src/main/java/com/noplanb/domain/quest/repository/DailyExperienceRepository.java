package com.noplanb.domain.quest.repository;

import com.noplanb.domain.quest.domain.DailyExperience;
import com.noplanb.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DailyExperienceRepository extends JpaRepository<DailyExperience, Long> {
    List<DailyExperience> findByCharacterIdAndDateBetweenOrderByDateAsc(Long characterId, LocalDate startDate, LocalDate endDate);

    void deleteAllByCharacterId(Long characterId);
}

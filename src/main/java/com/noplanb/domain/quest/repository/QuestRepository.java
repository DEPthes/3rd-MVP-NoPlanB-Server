package com.noplanb.domain.quest.repository;

import com.noplanb.domain.quest.domain.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuestRepository extends JpaRepository<Quest,Long> {
    List<Quest> findByCharacterIdAndCreatedTimeBetween(Long id, LocalDateTime startOfDay, LocalDateTime endOfDay);
}

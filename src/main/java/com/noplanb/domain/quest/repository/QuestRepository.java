package com.noplanb.domain.quest.repository;

import com.noplanb.domain.quest.domain.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestRepository extends JpaRepository<Quest,Long> {
}

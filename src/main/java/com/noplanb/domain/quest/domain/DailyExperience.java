package com.noplanb.domain.quest.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
public class DailyExperience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long characterId;
    private LocalDate date;
    private Long todayExp;

    public DailyExperience() {}

    public DailyExperience(Long userId, LocalDate date, Long experience) {
        this.characterId = userId;
        this.date = date;
        this.todayExp = experience;
    }

}

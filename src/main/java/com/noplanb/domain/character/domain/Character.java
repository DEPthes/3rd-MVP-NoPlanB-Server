package com.noplanb.domain.character.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.noplanb.domain.common.BaseEntity;
import com.noplanb.domain.item.domain.Item;
import com.noplanb.domain.quest.domain.Quest;
import com.noplanb.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "character_entity")
public class Character extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String characterName;
    private Long totalExp;
    private Long totalQuest;
    private Long todayExp;
    private Long level;

//    @OneToOne(mappedBy = "character", fetch = FetchType.LAZY)
//    private User user;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "character", fetch = FetchType.LAZY)
    private List<Quest> quests = new ArrayList<>();
    @OneToMany(mappedBy = "character")
    private List<Item> items = new ArrayList<>();



    public void addQuest(Quest quest){
        this.quests.add(quest);
        quest.updateCharacter(this);

    }
    public void addItem(Item item){
        this.items.add(item);
        item.updateCharacter(this);

    }


    public void updateCharacterName(String newCharacterName) {
        this.characterName = newCharacterName;
    }
    public void updateTodayExp(){
        this.todayExp = 0l;

    }

    @Builder
    public Character(String characterName, Long totalExp, Long totalQuest, Long todayExp, Long level, User user, List<Quest> quests, List<Item> items) {
        this.characterName = characterName;
        this.totalExp = totalExp;
        this.totalQuest = totalQuest;
        this.todayExp = todayExp;
        this.level = level;
        this.user = user;
        this.quests = quests;
        this.items = items;
    }
}

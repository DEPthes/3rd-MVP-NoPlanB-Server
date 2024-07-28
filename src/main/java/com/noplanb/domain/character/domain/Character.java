package com.noplanb.domain.character.domain;

import com.noplanb.domain.common.BaseEntity;
import com.noplanb.domain.item.domain.Item;
import com.noplanb.domain.quest.domain.Quest;
import jakarta.persistence.*;
import lombok.AccessLevel;
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
    @OneToMany(mappedBy = "character")
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
}

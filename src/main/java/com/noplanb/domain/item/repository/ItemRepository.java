package com.noplanb.domain.item.repository;

import com.noplanb.domain.character.domain.Character;
import com.noplanb.domain.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {
}

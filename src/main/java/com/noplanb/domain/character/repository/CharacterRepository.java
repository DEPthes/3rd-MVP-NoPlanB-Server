package com.noplanb.domain.character.repository;

import com.noplanb.domain.character.domain.Character;
import com.noplanb.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<Character,Long> {
    Optional<Character> findByUserId(Long id);

    boolean existsByUser(User user);

    void deleteByUser(User user);
}

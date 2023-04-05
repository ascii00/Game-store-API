package com.store.game.repositories;

import com.store.game.models.GameType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameTypeRepository extends JpaRepository<GameType, Integer> {
    GameType findByName(String name);
}

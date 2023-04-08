package com.store.game.repositories;

import com.store.game.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    Iterable<Game> findByNameContaining(String name);
    List<Game> findByGameTypeId(int gameTypeId);
}

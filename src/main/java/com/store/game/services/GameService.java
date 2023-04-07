package com.store.game.services;

import com.store.game.models.DTO.GameDTO;
import com.store.game.models.Game;

public interface GameService {
    Game getById(int id);
    Game getByName(String name);
    Iterable<Game> getAll();
    void update(int id, GameDTO newGame);
    void create(GameDTO game);
    void deleteById(int id);
}

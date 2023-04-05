package com.store.game.services;

import com.store.game.models.DTO.GameUpdate;
import com.store.game.models.Game;

public interface GameService {
    Game getById(int id);
    Game getByName(String name);
    Iterable<Game> getAll();
    void update(int id, GameUpdate newGame);
    void create(Game game);
    void deleteById(int id);
}

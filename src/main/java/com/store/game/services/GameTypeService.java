package com.store.game.services;

import com.store.game.models.GameType;

public interface GameTypeService {
    GameType getById(int id);
    Iterable<GameType> getAll();
    void create(GameType gameType);
    void deleteById(int id);
}

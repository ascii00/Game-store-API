package com.store.game.services;

import com.store.game.models.Game;
import com.store.game.models.GameType;

public interface GameTypeService {
    GameType getById(int id);
    Iterable<GameType> getAll();
}

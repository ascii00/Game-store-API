package com.store.game.services.implementation;

import com.store.game.models.GameType;
import com.store.game.repositories.GameTypeRepository;
import com.store.game.services.GameTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameTypeServiceImp implements GameTypeService {

    private final GameTypeRepository gameTypeRepository;

    @Override
    public GameType getById(int id) {
        Optional<GameType> gameType = gameTypeRepository.findById(id);
        if (gameType.isEmpty())
            throw new NoSuchElementException("Game type not found");
        return gameType.get();
    }

    @Override
    public Iterable<GameType> getAll() {
        return gameTypeRepository.findAll();
    }

    @Override
    public void create(GameType gameType) {
        if (gameTypeRepository.findByName(gameType.getName()) != null) {
            throw new IllegalArgumentException("Game type already exists");
        }
        gameTypeRepository.save(gameType);
    }

    @Override
    public void deleteById(int id) {
        if (gameTypeRepository.findById(id).isEmpty())
            throw new NoSuchElementException("Game type not found");
        gameTypeRepository.deleteById(id);
    }
}

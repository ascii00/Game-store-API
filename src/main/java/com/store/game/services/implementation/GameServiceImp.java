package com.store.game.services.implementation;

import com.store.game.models.Game;
import com.store.game.repositories.GameRepository;
import com.store.game.services.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameServiceImp implements GameService {

    private final GameRepository gameRepository;

    public Game getById(int id) {
        Optional<Game> game = gameRepository.findById(id);
        return game.orElse(null);
    }
}

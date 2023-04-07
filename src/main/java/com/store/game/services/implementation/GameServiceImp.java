package com.store.game.services.implementation;

import com.store.game.models.DTO.GameDTO;
import com.store.game.models.Game;
import com.store.game.models.GameType;
import com.store.game.repositories.GameRepository;
import com.store.game.repositories.GameTypeRepository;
import com.store.game.services.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameServiceImp implements GameService {

    private final GameRepository gameRepository;
    private final GameTypeRepository gameTypeRepository;

    public Game getById(int id) {
        Optional<Game> game = gameRepository.findById(id);
        if (game.isEmpty()) {
            throw new IllegalArgumentException("Game not found");
        }
        return game.get();
    }

    public Iterable<Game> getByName(String name) {
        return gameRepository.findByNameContaining(name);
    }

    public Iterable<Game> getAll() {
        return gameRepository.findAll();
    }

    public void create(GameDTO game) {
        Optional<GameType> gameType = Optional.ofNullable(gameTypeRepository.findByName(game.getGameType()));
        if (gameType.isEmpty()) {
            throw new IllegalArgumentException("Game type not found");
        }
        Game newGame = new Game(game.getName(),
                                game.getDescription(),
                                game.getPrice(),
                                gameType.get());
        gameRepository.save(newGame);
    }

    public void deleteById(int id) {
        if (gameRepository.findById(id).isEmpty()){
            throw new IllegalArgumentException("Game not found");
        }
        gameRepository.deleteById(id);
    }

    public void update(int id, GameDTO newGame) {
        Optional<Game> gameToEdit = gameRepository.findById(id);
        Optional<GameType> gameType = Optional.ofNullable(gameTypeRepository.findByName(newGame.getGameType()));
        if (gameType.isEmpty()) {
            throw new IllegalArgumentException("Game type not found");
        }
        if (gameToEdit.isEmpty()) {
            throw new IllegalArgumentException("Game not found");
        }
        Game game = gameToEdit.get();
        game.setName(newGame.getName());
        game.setDescription(newGame.getDescription());
        game.setPrice(newGame.getPrice());
        game.setGameType(gameType.get());
        gameRepository.save(game);
    }
}

package com.store.game.service;

import com.store.game.models.Game;
import com.store.game.models.GameType;
import com.store.game.repositories.GameRepository;
import com.store.game.repositories.GameTypeRepository;
import com.store.game.services.GameService;
import com.store.game.services.GameTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @InjectMocks
    private GameService gameService;
    @Mock
    private GameRepository gameRepository;

    @Test
    void getById() {
        GameType gameType = new GameType("RPG");
        Game game = new Game("TestGame", "TestDesc", 1.50, gameType);
        game.setId(1);

        when(gameRepository.findById(1)).thenReturn(Optional.of(game));

        Game result = gameService.getById(1);
        assertEquals(game, result);
    }

    @Test
    void getByIdNotFound() {
    }

    @Test
    void getByName() {
    }

    @Test
    void getByNameNotFound() {
    }

    @Test
    void getAll() {
    }

    @Test
    void update() {
    }

    @Test
    void create() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void deleteByIdNotFound() {
    }
}

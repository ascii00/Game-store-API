package com.store.game.service;

import com.store.game.models.DTO.GameDTO;
import com.store.game.models.Game;
import com.store.game.models.GameType;
import com.store.game.repositories.GameRepository;
import com.store.game.repositories.GameTypeRepository;
import com.store.game.services.implementation.GameServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @InjectMocks
    private GameServiceImp gameService;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private GameTypeRepository gameTypeRepository;

    @Test
    void getById() {
        GameType gameType = new GameType("RPG");
        Game game = new Game("TestGame", "TestDesc", 1.50, gameType);
        game.setId(1);

        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        Game result = gameService.getById(1);

        assertEquals(game, result);
        verify(gameRepository, times(1)).findById(1);

    }

    @Test
    void getByIdNotFound() {
        when(gameRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> gameService.getById(1));
    }

    @Test
    void getByName() {
        GameType gameType = new GameType("RPG");
        Game game = new Game("TestGame", "TestDesc", 1.50, gameType);
        game.setId(1);

        when(gameRepository.findByName("TestGame")).thenReturn(game);
        Game result = gameService.getByName("TestGame");

        assertEquals(game, result);
        verify(gameRepository, times(1)).findByName("TestGame");
    }

    @Test
    void getByNameNotFound() {
        when(gameRepository.findByName("TestGame")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> gameService.getByName("TestGame"));
    }

    @Test
    void getAll() {
        GameType gameType = new GameType("RPG");
        Game game1 = new Game("TestGame1", "TestDesc1", 1.50, gameType);
        Game game2 = new Game("TestGame2", "TestDesc2", 1.50, gameType);

        List<Game> games = Arrays.asList(game1, game2);
        when(gameRepository.findAll()).thenReturn(games);
        Iterable<Game> result = gameService.getAll();

        assertEquals(games, result);
        verify(gameRepository, times(1)).findAll();
    }

    @Test
    void update() {
        GameType gameType = new GameType("RPG");
        Game game = new Game("TestGame", "TestDesc", 1.50, gameType);
        game.setId(1);

        GameDTO updatedGame = new GameDTO("UpdatedGame", "UpdatedDesc", 2.00, "RPG");

        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        when(gameTypeRepository.findByName("RPG")).thenReturn(gameType);
        gameService.update(1, updatedGame);

        assertEquals("UpdatedGame", game.getName());
        assertEquals("UpdatedDesc", game.getDescription());
        assertEquals(2.00, game.getPrice());
        assertEquals(gameType, game.getGameType());
        verify(gameRepository, times(1)).save(game);
    }

    @Test
    void updateNotFound() {
        GameDTO updatedGame = new GameDTO("UpdatedGame", "UpdatedDesc", 2.00, "RPG");

        when(gameRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> gameService.update(1, updatedGame));
    }

    @Test
    void updateNotFoundGameType() {
        GameType gameType = new GameType("RPG");
        Game game = new Game("TestGame", "TestDesc", 1.50, gameType);
        game.setId(1);

        GameDTO updatedGame = new GameDTO("UpdatedGame", "UpdatedDesc", 2.00, "RPG");

        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        when(gameTypeRepository.findByName("RPG")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> gameService.update(1, updatedGame));
    }

    @Test
    void create() {
        GameType gameType = new GameType("RPG");
        GameDTO gameDTO = new GameDTO("NewGame", "NewDesc", 1.50, "RPG");

        when(gameTypeRepository.findByName("RPG")).thenReturn(gameType);

        gameService.create(gameDTO);

        ArgumentCaptor<Game> gameCaptor = ArgumentCaptor.forClass(Game.class);
        verify(gameRepository, times(1)).save(gameCaptor.capture());
        Game capturedGame = gameCaptor.getValue();

        assertEquals("NewGame", capturedGame.getName());
        assertEquals("NewDesc", capturedGame.getDescription());
        assertEquals(1.50, capturedGame.getPrice());
        assertEquals(gameType, capturedGame.getGameType());
    }

    @Test
    void deleteById() {
        GameType gameType = new GameType("RPG");
        Game game = new Game("TestGame", "TestDesc", 1.50, gameType);
        game.setId(1);

        when(gameRepository.findById(1)).thenReturn(Optional.of(game));
        gameService.deleteById(1);

        verify(gameRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteByIdNotFound() {
        when(gameRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> gameService.deleteById(1));
    }
}

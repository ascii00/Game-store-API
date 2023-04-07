package com.store.game.controllers;

import com.store.game.models.DTO.GameDTO;
import com.store.game.models.Game;
import com.store.game.models.GameType;
import com.store.game.response.Response;
import com.store.game.response.SuccessResponse;
import com.store.game.services.GameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameControllerTest {
    @InjectMocks
    private GameController gameController;

    @Mock
    private GameService gameService;

    @Test
    void getById() {
        GameType gameType = new GameType("RPG");
        Game game = new Game("TestGame", "TestDesc", 1.50, gameType);
        game.setId(1);

        when(gameService.getById(1)).thenReturn(game);
        ResponseEntity<Response> response = gameController.getById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(game, ((SuccessResponse<Game>) response.getBody()).getData());
        verify(gameService, times(1)).getById(1);
    }

    @Test
    void getByName() {
        GameType gameType = new GameType("RPG");
        Game game = new Game("TestGame", "TestDesc", 1.50, gameType);
        game.setId(1);

        when(gameService.getByName("TestGame")).thenReturn(game);
        ResponseEntity<Response> response = gameController.getByName("TestGame");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(game, ((SuccessResponse<Game>) response.getBody()).getData());
        verify(gameService, times(1)).getByName("TestGame");
    }

    @Test
    void getAll() {
        GameType gameType = new GameType("RPG");
        Game game1 = new Game("TestGame", "TestDesc", 1.50, gameType);
        Game game2 = new Game("TestGame", "TestDesc", 1.50, gameType);

        List<Game> games = Arrays.asList(game1, game2);
        when(gameService.getAll()).thenReturn(games);
        ResponseEntity<Response> response = gameController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(games, ((SuccessResponse<Iterable>) response.getBody()).getData());
        verify(gameService, times(1)).getAll();
    }

    @Test
    void createGame() {
        GameDTO game = new GameDTO("TestGame", "TestDesc", 1.50, "RPG");
        ResponseEntity<Response> response = gameController.createGame(game);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNull(((SuccessResponse<?>) response.getBody()).getData());
        verify(gameService, times(1)).create(game);
    }

    @Test
    void updateGame() {
        int gameId = 1;
        GameDTO game = new GameDTO("TestGame", "TestDesc", 1.50, "RPG");

        ResponseEntity<Response> response = gameController.updateGame(gameId, game);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(((SuccessResponse<?>) response.getBody()).getData());

        verify(gameService, times(1)).update(gameId, game);
    }

    @Test
    void deleteGame() {
        int gameId = 1;
        ResponseEntity<Response> response = gameController.deleteGame(gameId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(((SuccessResponse<?>) response.getBody()).getData());
        verify(gameService, times(1)).deleteById(gameId);
    }
}

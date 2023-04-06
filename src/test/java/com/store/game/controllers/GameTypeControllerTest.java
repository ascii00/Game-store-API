package com.store.game.controllers;

import com.store.game.models.GameType;
import com.store.game.response.Response;
import com.store.game.response.SuccessResponse;
import com.store.game.services.GameTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameTypeControllerTest {
    @InjectMocks
    private GameTypeController gameTypeController;

    @Mock
    private GameTypeService gameTypeService;

    @Test
    void getById() {
        GameType gameType = new GameType("RPG");
        gameType.setId(1);

        when(gameTypeService.getById(1)).thenReturn(gameType);
        ResponseEntity<Response> response = gameTypeController.getById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(gameType, ((SuccessResponse<GameType>) response.getBody()).getData());
        verify(gameTypeService, times(1)).getById(1);
    }

    @Test
    void getAll() {
        GameType gameType1 = new GameType("RPG");
        GameType gameType2 = new GameType("Action");

        List<GameType> gameTypes = Arrays.asList(gameType1, gameType2);
        when(gameTypeService.getAll()).thenReturn(gameTypes);
        ResponseEntity<Response> response = gameTypeController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(gameTypes, ((SuccessResponse<Iterable<GameType>>) response.getBody()).getData());
        verify(gameTypeService, times(1)).getAll();
    }

    @Test
    void deleteGameType() {
        int gameId = 1;
        ResponseEntity<Response> response = gameTypeController.deleteGameType(gameId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(((SuccessResponse<?>) response.getBody()).getData());
        verify(gameTypeService, times(1)).deleteById(gameId);
    }

    @Test
    void createGameType() {
        GameType gameType = new GameType("RPG");
        ResponseEntity<Response> response = gameTypeController.createGameType(gameType);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNull(((SuccessResponse<?>) response.getBody()).getData());
        verify(gameTypeService, times(1)).create(gameType);
    }
}

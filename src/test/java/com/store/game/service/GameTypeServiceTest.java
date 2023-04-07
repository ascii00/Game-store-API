package com.store.game.service;

import com.store.game.models.GameType;
import com.store.game.repositories.GameTypeRepository;
import com.store.game.services.implementation.GameTypeServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
public class GameTypeServiceTest {

    @InjectMocks
    private GameTypeServiceImp gameTypeService;
    @Mock
    private GameTypeRepository gameTypeRepository;

    @Test
    void getById() {
        GameType gameType = new GameType("RPG");
        gameType.setId(1);

        when(gameTypeRepository.findById(1)).thenReturn(Optional.of(gameType));

        GameType result = gameTypeService.getById(1);
        assertEquals(gameType, result);
        verify(gameTypeRepository, times(1)).findById(1);

    }

    @Test
    void getByIdNotFound() {
        when(gameTypeRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> gameTypeService.getById(1));
    }

    @Test
    void getAll() {
        GameType gameType1 = new GameType("RPG");
        GameType gameType2 = new GameType("Action");

        List<GameType> gameTypes = Arrays.asList(gameType1, gameType2);
        when(gameTypeRepository.findAll()).thenReturn(gameTypes);
        Iterable<GameType> result = gameTypeService.getAll();

        assertEquals(gameTypes, result);
        verify(gameTypeRepository, times(1)).findAll();
    }

    @Test
    void create() {
        GameType gameType = new GameType("RPG");
        gameType.setId(1);

        when(gameTypeRepository.save(gameType)).thenReturn(gameType);

        gameTypeService.create(gameType);
        verify(gameTypeRepository, times(1)).save(gameType);
    }

    @Test
    void deleteById() {
        GameType gameType = new GameType("RPG");
        gameType.setId(1);

        when(gameTypeRepository.findById(1)).thenReturn(Optional.of(gameType));

        gameTypeService.deleteById(1);
        verify(gameTypeRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteByIdNotFound() {
        when(gameTypeRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> gameTypeService.deleteById(1));
    }
}




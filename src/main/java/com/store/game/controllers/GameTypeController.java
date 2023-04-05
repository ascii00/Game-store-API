package com.store.game.controllers;

import com.store.game.models.GameType;
import com.store.game.response.Response;
import com.store.game.response.SuccessResponse;
import com.store.game.services.GameTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/gameType")
@RequiredArgsConstructor
public class GameTypeController {

    private final GameTypeService gameTypeService;

    @GetMapping("/{id}")
    public ResponseEntity<Response> getById(@PathVariable Integer id) {
        GameType type = gameTypeService.getById(id);
        return ResponseEntity.ok().body(new SuccessResponse<>(type));
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAll() {
        Iterable<GameType> gameTypes = gameTypeService.getAll();
        return ResponseEntity.ok().body(new SuccessResponse<>(gameTypes));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> deleteGameType(@PathVariable Integer id) {
        gameTypeService.deleteById(id);
        return ResponseEntity.ok().body(new SuccessResponse<>(null));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> createGameType(@RequestBody GameType gameType) {
        gameTypeService.create(gameType);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>(null));
    }
}

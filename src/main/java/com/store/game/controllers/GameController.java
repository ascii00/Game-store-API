package com.store.game.controllers;

import com.store.game.models.DTO.GameDTO;
import com.store.game.models.Game;
import com.store.game.response.SuccessResponse;
import com.store.game.services.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.store.game.response.Response;

@RestController
@RequestMapping("/api/v1/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @GetMapping("/byId/{id}")
    public ResponseEntity<Response> getById(@PathVariable Integer id) {
        Game game = gameService.getById(id);
        return ResponseEntity.ok().body(new SuccessResponse<>(game));
    }

    @GetMapping("/byName/{name}")
    public ResponseEntity<Response> getByName(@PathVariable String name) {
        Game game = gameService.getByName(name);
        return ResponseEntity.ok().body(new SuccessResponse<>(game));
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAll() {
        Iterable<Game> games = gameService.getAll();
        return ResponseEntity.ok().body(new SuccessResponse<>(games));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> createGame(@RequestBody GameDTO game) {
        gameService.create(game);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>(null));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> updateGame(@PathVariable Integer id, @RequestBody GameDTO game) {
        gameService.update(id, game);
        return ResponseEntity.ok().body(new SuccessResponse<>(null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> deleteGame(@PathVariable Integer id) {
        gameService.deleteById(id);
        return ResponseEntity.ok().body(new SuccessResponse<>(null));
    }
}

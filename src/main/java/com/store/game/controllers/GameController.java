package com.store.game.controllers;

import com.store.game.models.Game;
import com.store.game.response.FailResponse;
import com.store.game.response.SuccessResponse;
import com.store.game.services.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.store.game.response.Response;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/v1/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService service;

    @GetMapping("/{id}")
    public ResponseEntity<Response> getById(@PathVariable Integer id) {
        Game game = service.getById(id);
        if (game == null) {
            return ResponseEntity.status(NOT_FOUND).body(new FailResponse<>("Game not found"));
        }
        return ResponseEntity.ok().body(new SuccessResponse<>(game));
    }

}

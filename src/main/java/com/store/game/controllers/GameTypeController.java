package com.store.game.controllers;

import com.store.game.models.GameType;
import com.store.game.response.Response;
import com.store.game.response.SuccessResponse;
import com.store.game.services.GameTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}

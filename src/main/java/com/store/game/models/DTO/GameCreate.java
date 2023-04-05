package com.store.game.models.DTO;

import lombok.Data;

@Data
public class GameCreate {
    private String name;
    private String description;
    private Double price;
    private String gameType;
}

package com.store.game.models.DTO;

import lombok.Data;

@Data
public class GameUpdate {
    private String name;
    private String description;
    private Double price;
    private String type;
}

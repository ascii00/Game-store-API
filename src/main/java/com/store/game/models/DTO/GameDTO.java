package com.store.game.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameDTO {
    private String name;
    private String description;
    private Double price;
    private String gameType;
}

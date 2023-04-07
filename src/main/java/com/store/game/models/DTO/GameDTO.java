package com.store.game.models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name cannot be less that 2 and more than 50 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(min = 2, max = 50, message = "Description cannot be less that 2 and more than 50 characters")
    private String description;

    @Positive
    private Double price;

    @NotBlank(message = "Game type is required")
    private String gameType;
}

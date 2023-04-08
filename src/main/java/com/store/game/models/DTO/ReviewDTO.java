package com.store.game.models.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewDTO {

    @NotNull(message = "Rating cannot be null")
    @Max(value = 5, message = "Rating cannot be more than 5")
    @Min(value = 1, message = "Rating cannot be less than 1")
    private int rating;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 2, max = 500, message = "Description cannot be less than 2 and more than 500 characters")
    private String description;

    @NotNull(message = "Game is required")
    private int gameId;

    public ReviewDTO(int rating, String description, int gameId) {
        this.rating = rating;
        this.description = description;
        this.gameId = gameId;
    }
}

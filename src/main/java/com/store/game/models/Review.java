package com.store.game.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Entity
@Table(name = "Review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Rating cannot be null")
    @Column(name = "rating", nullable = false)
    @Max(value = 5, message = "Rating cannot be more than 5")
    @Min(value = 1, message = "Rating cannot be less than 1")
    private int rating;

    @Column(name = "description", nullable = false)
    @NotBlank(message = "Description cannot be blank")
    @Length(min = 2, max = 500, message = "Description cannot be less than 2 and more than 500 characters")
    private String description;

    @JsonIgnore
    @NotNull(message = "Game is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_ID")
    private Game game;

    public Review() {
    }

    public Review(int rating, String description, Game game) {
        this.rating = rating;
        this.description = description;
        this.game = game;
    }
}

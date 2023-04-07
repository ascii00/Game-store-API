package com.store.game.models;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.*;

import java.util.List;

@Data
@Entity
@Table(name = "Game")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name cannot be less that 2 and more than 50 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Description is required")
    @Size(min = 2, max = 50, message = "Description cannot be less that 2 and more than 50 characters")
    @Column(name = "description", nullable = false)
    private String description;

    @Positive
    @Column(name = "price", nullable = false)
    private double price;

    @NotNull(message = "Game type is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_type_ID")
    private GameType gameType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "game")
    private List<Review> reviews;

    public Game() {
    }

    public Game(String name, String description, double price, GameType gameType) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.gameType = gameType;
    }
}

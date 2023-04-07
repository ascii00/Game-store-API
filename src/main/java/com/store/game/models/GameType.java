package com.store.game.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "Game_Type")
public class GameType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name cannot be less that 2 and more than 50 characters")
    @Column(name = "name", nullable = false)
    private String name;

    public GameType() {
    }

    public GameType(String name) {
        this.name = name;
    }
}

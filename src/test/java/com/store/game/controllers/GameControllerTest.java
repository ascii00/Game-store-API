package com.store.game.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.game.models.DTO.GameDTO;
import com.store.game.models.Game;
import com.store.game.models.GameType;
import com.store.game.repositories.GameRepository;
import com.store.game.repositories.GameTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.config.name=application-test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GameTypeRepository gameTypeRepository;
    private GameType gameType;
    private Game game1;
    private Game game2;

    @BeforeEach
    public void setup() {
        gameRepository.deleteAll();
        gameTypeRepository.deleteAll();

        gameType = new GameType("Action");
        gameTypeRepository.save(gameType);
        game1 = new Game("Game 1", "Game 1 description", 10.0, gameType);
        game2 = new Game("Game 2", "Game 2 description", 15.0, gameType);
        gameRepository.saveAll(List.of(game1, game2));
    }

    @Test
    public void getById() throws Exception {
        mockMvc.perform(get("/api/v1/game/byId/{id}", game1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", is("Game 1")))
                .andExpect(jsonPath("$.data.description", is("Game 1 description")))
                .andExpect(jsonPath("$.data.price", is(10.0)));
    }

    @Test
    public void getByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/game/byId/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getByName() throws Exception {
        mockMvc.perform(get("/api/v1/game/byName/Game 1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].name", is("Game 1")))
                .andExpect(jsonPath("$.data[0].description", is("Game 1 description")))
                .andExpect(jsonPath("$.data[0].price", is(10.0)));
    }

    @Test
    public void getAll() throws Exception {
        mockMvc.perform(get("/api/v1/game/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].name", is("Game 1")))
                .andExpect(jsonPath("$.data[1].name", is("Game 2")));
    }

    @Test
    public void UnauthorizedUpdate() throws Exception {
        GameDTO gameDTO = new GameDTO("Updated Game 1", "Updated Game 1 description", 12.0, gameType.getName());

        mockMvc.perform(put("/api/v1/game/{id}", game1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(gameDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void updateWithUserRole() throws Exception {
        GameDTO gameDTO = new GameDTO("Updated Game 1", "Updated Game 1 description", 12.0, gameType.getName());

        mockMvc.perform(put("/api/v1/game/{id}", game1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(gameDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateGame() throws Exception {
        GameDTO gameDTO = new GameDTO("Updated Game 1", "Updated Game 1 description", 12.0, gameType.getName());

        mockMvc.perform(put("/api/v1/game/{id}", game1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(gameDTO)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/game/byId/{id}", game1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", is("Updated Game 1")))
                .andExpect(jsonPath("$.data.description", is("Updated Game 1 description")))
                .andExpect(jsonPath("$.data.price", is(12.0)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateGameNotFound() throws Exception {
        GameDTO gameDTO = new GameDTO("Updated Game 1", "Updated Game 1 description", 12.0, gameType.getName());

        mockMvc.perform(put("/api/v1/game/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(gameDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateGameTypeNotFound() throws Exception {
        GameDTO gameDTO = new GameDTO("Updated Game 1", "Updated Game 1 description", 12.0, "Invalid Game Type");

        mockMvc.perform(put("/api/v1/game/{id}", game1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(gameDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateGameInvalidPrice() throws Exception {
        GameDTO gameDTO = new GameDTO("Updated Game 1", "Updated Game 1 description", -12.0, gameType.getName());

        mockMvc.perform(put("/api/v1/game/{id}", game1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(gameDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void UnauthorizedDelete() throws Exception {
        mockMvc.perform(delete("/api/v1/game/{id}", game1.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void deleteWithUserRole() throws Exception {
        mockMvc.perform(delete("/api/v1/game/{id}", game1.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void deleteGame() throws Exception {
        mockMvc.perform(delete("/api/v1/game/{id}", game1.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/game/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].name", is("Game 2")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void deleteGameNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/game/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void UnauthorizedCreate() throws Exception {
        GameDTO gameDTO = new GameDTO("Game 3", "Game 3 description", 20.0, gameType.getName());

        mockMvc.perform(post("/api/v1/game")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(gameDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void createWithUserRole() throws Exception {
        GameDTO gameDTO = new GameDTO("Game 3", "Game 3 description", 20.0, gameType.getName());

        mockMvc.perform(post("/api/v1/game")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(gameDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createGame() throws Exception {
        GameDTO gameDTO = new GameDTO("Game 3", "Game 3 description", 20.0, gameType.getName());

        mockMvc.perform(post("/api/v1/game")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(gameDTO)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/game/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[2].name", is("Game 3")))
                .andExpect(jsonPath("$.data[2].description", is("Game 3 description")))
                .andExpect(jsonPath("$.data[2].price", is(20.0)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createGameInvalidPrice() throws Exception {
        GameDTO gameDTO = new GameDTO("Game 3", "Game 3 description", -20.0, gameType.getName());

        mockMvc.perform(post("/api/v1/game")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(gameDTO)))
                .andExpect(status().isBadRequest());
    }
}

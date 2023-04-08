package com.store.game.controllers;

import com.store.game.models.GameType;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.config.name=application-test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GameTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GameTypeRepository gameTypeRepository;
    private GameType gameType1;
    private GameType gameType2;

    @BeforeEach
    public void setUp() {
        gameTypeRepository.deleteAll();

        gameType1 = new GameType("Action");
        gameType2 = new GameType("Adventure");

        gameTypeRepository.save(gameType1);
        gameTypeRepository.save(gameType2);
    }

    @Test
    public void getAllGameTypes() throws Exception {
        mockMvc.perform(get("/api/v1/gameType/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].name", is("Action")))
                .andExpect(jsonPath("$.data[1].name", is("Adventure")));
    }

    @Test
    public void getGameTypeById() throws Exception {
        mockMvc.perform(get("/api/v1/gameType/{id}", gameType1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", is("Action")));
    }

    @Test
    public void getGameTypeByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/gameType/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void unauthorizedCreate() throws Exception {
        mockMvc.perform(post("/api/v1/gameType")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Strategy\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void createWithUserRole() throws Exception {
        mockMvc.perform(post("/api/v1/gameType")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Strategy\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createGameType() throws Exception {
        mockMvc.perform(post("/api/v1/gameType")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Strategy\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/gameType/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[2].name", is("Strategy")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createGameTypeAlreadyExists() throws Exception {
        mockMvc.perform(post("/api/v1/gameType")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Action\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void unauthorizedDelete() throws Exception {
        mockMvc.perform(delete("/api/v1/gameType/{id}", gameType1.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteWithUserRole() throws Exception {
        mockMvc.perform(delete("/api/v1/gameType/{id}", gameType1.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void deleteGameType() throws Exception {
        mockMvc.perform(delete("/api/v1/gameType/{id}", gameType1.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/gameType/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void deleteGameTypeNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/gameType/-1"))
                .andExpect(status().isNotFound());
    }
}

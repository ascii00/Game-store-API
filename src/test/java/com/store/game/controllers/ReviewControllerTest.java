package com.store.game.controllers;

import com.store.game.models.Game;
import com.store.game.models.GameType;
import com.store.game.models.Review;
import com.store.game.repositories.GameRepository;
import com.store.game.repositories.GameTypeRepository;
import com.store.game.repositories.ReviewRepository;
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
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GameTypeRepository gameTypeRepository;
    private GameType gameType;
    private Game game;
    private Review review;

    @BeforeEach
    public void setup() {
        reviewRepository.deleteAll();
        gameRepository.deleteAll();
        gameTypeRepository.deleteAll();
        gameType = new GameType("Action");
        game = new Game("Game 1", "Game 1 description", 10.0, gameType);
        review = new Review(5, "Review 1 description", game);
        gameTypeRepository.save(gameType);
        gameRepository.save(game);
        reviewRepository.save(review);
    }

    @Test
    public void getAll() throws Exception {
        mockMvc.perform(get("/api/v1/review"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].rating", is(5)))
                .andExpect(jsonPath("$.data[0].description", is("Review 1 description")));
    }

    @Test
    public void addReviewUnauthorized() throws Exception {
        String content = "{\"rating\": 4, \"description\": \"Review 2 description\", \"gameId\": " + game.getId() + "}";
        mockMvc.perform(post("/api/v1/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addReview() throws Exception {
        String content = "{\"rating\": 4, \"description\": \"Review 2 description\", \"gameId\": " + game.getId() + "}";
        mockMvc.perform(post("/api/v1/review")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateReviewUnauthorized() throws Exception {
        String content = "{\"rating\": 4, \"description\": \"Review 2 description\", \"gameId\": " + game.getId() + "}";
        mockMvc.perform(put("/api/v1/review/{id}", review.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void updateReviewWithUserRole() throws Exception {
        String content = "{\"rating\": 4, \"description\": \"Review 2 description\", \"gameId\": " + game.getId() + "}";
        mockMvc.perform(put("/api/v1/review/{id}", review.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateReview() throws Exception {
        String content = "{\"rating\": 4, \"description\": \"Review 2 description\", \"gameId\": " + game.getId() + "}";
        mockMvc.perform(put("/api/v1/review/{id}", review.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateReviewNotFound() throws Exception {
        String content = "{\"rating\": 4, \"description\": \"Review 2 description\", \"gameId\": " + game.getId() + "}";
        mockMvc.perform(put("/api/v1/review/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteReviewUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/v1/review/{id}", review.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteReviewWithUserRole() throws Exception {
        mockMvc.perform(delete("/api/v1/review/{id}", review.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void deleteReview() throws Exception {
        mockMvc.perform(delete("/api/v1/review/{id}", review.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void deleteReviewNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/review/-1"))
                .andExpect(status().isNotFound());
    }
}

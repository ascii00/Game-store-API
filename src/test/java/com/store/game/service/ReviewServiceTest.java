package com.store.game.service;

import com.store.game.models.DTO.ReviewDTO;
import com.store.game.models.Game;
import com.store.game.models.Review;
import com.store.game.repositories.GameRepository;
import com.store.game.repositories.ReviewRepository;
import com.store.game.services.implementation.ReviewServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @InjectMocks
    private ReviewServiceImp reviewService;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private GameRepository gameRepository;

    @Test
    void getAll() {
        Review review1 = new Review(5, "Great game!", new Game());
        Review review2 = new Review(3, "Good game, but could be better.", new Game());

        List<Review> reviews = Arrays.asList(review1, review2);
        when(reviewRepository.findAll()).thenReturn(reviews);

        Iterable<Review> result = reviewService.getAll();
        assertEquals(reviews, result);
        verify(reviewRepository, times(1)).findAll();
    }

    @Test
    void create() {
        int gameId = 1;
        Game game = new Game();
        game.setId(gameId);
        ReviewDTO reviewDTO = new ReviewDTO(5, "Amazing game!", gameId);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        reviewService.create(reviewDTO);
        ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository, times(1)).save(reviewCaptor.capture());

        Review capturedReview = reviewCaptor.getValue();
        assertEquals(reviewDTO.getRating(), capturedReview.getRating());
        assertEquals(reviewDTO.getDescription(), capturedReview.getDescription());
        assertEquals(game, capturedReview.getGame());
    }

    @Test
    void createGameNotFound() {
        int gameId = 1;
        ReviewDTO reviewDTO = new ReviewDTO(5, "Amazing game!", gameId);

        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> reviewService.create(reviewDTO));
    }

    @Test
    void deleteById() {
        int reviewId = 1;
        Review review = new Review(5, "Great game!", new Game());
        review.setId(reviewId);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        reviewService.deleteById(reviewId);
        verify(reviewRepository, times(1)).deleteById(reviewId);
    }

    @Test
    void deleteByIdNotFound() {
        int reviewId = 1;

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> reviewService.deleteById(reviewId));
    }

    @Test
    void update() {
        int gameId = 1;
        Game game = new Game();
        game.setId(gameId);
        Review review = new Review(5, "Great game!", game);
        review.setId(1);

        ReviewDTO updatedReview = new ReviewDTO(4, "Good game, but could be better.", gameId);

        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        reviewService.update(review.getId(), updatedReview);

        assertEquals(updatedReview.getRating(), review.getRating());
        assertEquals(updatedReview.getDescription(), review.getDescription());
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void updateReviewNotFound() {
        int reviewId = 1;
        int gameId = 1;
        ReviewDTO updatedReview = new ReviewDTO(4, "Good game, but could be better.", gameId);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> reviewService.update(reviewId, updatedReview));
    }

    @Test
    void updateGameNotFound() {
        int reviewId = 1;
        int gameId = 1;
        Review review = new Review(5, "Great game!", new Game());
        review.setId(reviewId);

        ReviewDTO updatedReview = new ReviewDTO(4, "Good game, but could be better.", gameId);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> reviewService.update(reviewId, updatedReview));
    }
}

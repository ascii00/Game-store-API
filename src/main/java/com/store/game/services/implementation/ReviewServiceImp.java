package com.store.game.services.implementation;

import com.store.game.models.DTO.ReviewDTO;
import com.store.game.models.Game;
import com.store.game.models.Review;
import com.store.game.repositories.GameRepository;
import com.store.game.repositories.ReviewRepository;
import com.store.game.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImp implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final GameRepository gameRepository;

    public Iterable<Review> getAll() {
        return reviewRepository.findAll();
    }

    public void create(ReviewDTO review) {
        Optional<Game> game = gameRepository.findById(review.getGameId());
        if (game.isEmpty()) {
            throw new IllegalArgumentException("Game not found");
        }
        Review newReview = new Review(review.getRating(),
                                      review.getDescription(),
                                      game.get());
        reviewRepository.save(newReview);
    }

    public void deleteById(int id) {
        if (reviewRepository.findById(id).isEmpty()){
            throw new IllegalArgumentException("Review not found");
        }
        reviewRepository.deleteById(id);
    }

    public void update(int id, ReviewDTO updatedReview) {
        Optional<Review> reviewToEdit = reviewRepository.findById(id);
        Optional<Game> game = gameRepository.findById(updatedReview.getGameId());
        if (reviewToEdit.isEmpty()) {
            throw new IllegalArgumentException("Review not found");
        }
        if (game.isEmpty()) {
            throw new IllegalArgumentException("Game not found");
        }
        Review review = reviewToEdit.get();
        review.setRating(updatedReview.getRating());
        review.setDescription(updatedReview.getDescription());
        review.setGame(game.get());
        reviewRepository.save(review);
    }
}

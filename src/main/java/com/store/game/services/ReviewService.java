package com.store.game.services;

import com.store.game.models.DTO.ReviewDTO;
import com.store.game.models.Review;

public interface ReviewService {
    Iterable<Review> getAll();
    void create(ReviewDTO review);
    void deleteById(int id);
    void update(int id, ReviewDTO updatedReview);
}

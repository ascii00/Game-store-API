package com.store.game.controllers;

import com.store.game.models.DTO.ReviewDTO;
import com.store.game.models.Review;
import com.store.game.response.Response;
import com.store.game.response.SuccessResponse;
import com.store.game.services.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<Response> getAllReviews() {
        Iterable<Review> reviews = reviewService.getAll();
        return ResponseEntity.ok().body(new SuccessResponse<>(reviews));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Response> addReview(@Valid @RequestBody ReviewDTO review) {
        reviewService.create(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>(null));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> updateReview(@PathVariable("id") int id, @Valid @RequestBody ReviewDTO updatedReview) {
        reviewService.update(id, updatedReview);
        return ResponseEntity.ok().body(new SuccessResponse<>(null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Response> deleteReview(@PathVariable("id") int id) {
        reviewService.deleteById(id);
        return ResponseEntity.ok().body(new SuccessResponse<>(null));
    }

}

package com.backend.controller;

import com.backend.model.Review;
import com.backend.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @PostMapping(value = "/reviews/create/{userId}")
    public ResponseEntity<String> createReview(@RequestBody Review review, @PathVariable("userId") int userId) {
        return reviewService.createReview(review, userId);
    }


    @PutMapping(value="/reviews/create/")
    public ResponseEntity<String> updateReview(@RequestBody Review review) {
    return reviewService.updateReview(review);
    }

    @GetMapping(value="/reviews/{reviewId}")
    public ResponseEntity<Review> getReviewById(@PathVariable("reviewId") int reviewId) {
        return reviewService.getReviewById(reviewId);
    }

    @GetMapping(value="/reviews/user/{userId}")
    public ResponseEntity<List<Review>> getReviewsByUserId(@PathVariable("userId") int userId) {
        return reviewService.getReviewsByUserId(userId);
    }

    @GetMapping(value="/reviews/movie/{movieId}")
    public ResponseEntity<List<Review>> getReviewsByMovieId(@PathVariable("movieId") int movieId) {
        return reviewService.getReviewsByMovieId(movieId);
    }

    @GetMapping(value = "/reviews/user/{userId}/date/{firstDate}/{lastDate}")
    public ResponseEntity<List<Review>> getReviewsByDate(@PathVariable("userId") int userId,
                                                         @PathVariable("firstDate") String firstDate,
                                                         @PathVariable("lastDate") String lastDate){

        return reviewService.getReviewsByDate(userId, firstDate, lastDate);

    }

    @GetMapping(value="/reviews/movie/{movieId}/rating/{rating}")
    public ResponseEntity<Integer> getAmountOfRating(@PathVariable("movieId") int movieId,
                                                     @PathVariable("rating") int rating) {
        return reviewService.getAmountOfRating(movieId, rating);
    }


}

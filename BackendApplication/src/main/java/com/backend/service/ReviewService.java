package com.backend.service;

import com.backend.model.Movie;
import com.backend.model.Review;
import com.backend.repository.MovieRepository;
import com.backend.repository.ReviewRepository;
import com.fasterxml.jackson.databind.ser.std.StdArraySerializers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    MovieRepository movieRepository;



    public ResponseEntity<String> createReview(Review review, int id) {

        List<Review> reviewList = reviewRepository.findAll();
        List<Review> reviewsByUser = new LinkedList<>();

        if (!reviewList.isEmpty()) {
            for (Review rev : reviewList) {
                if (rev.getUser().getAccountID() == id) {
                    reviewsByUser.add(rev);
                }
            }
            for(Review rev : reviewsByUser){
                if(rev.getMovie().getMovieId() == review.getMovie().getMovieId()){
                    return new ResponseEntity<>("You already posted a Review to that Movie!", HttpStatus.BAD_REQUEST);
                }
            }
        }
        reviewRepository.save(review);
        Movie movie = movieRepository.findById(review.getMovie().getMovieId()).get();

        movie.setReviewCount(movie.getReviewCount()+1);
        movie.setAverageRating(setAverageRatingOfMovie(movie));
        movieRepository.save(movie);

        return new ResponseEntity<>("Review created!", HttpStatus.OK);
    }

    public ResponseEntity<String> updateReview(Review review){
        reviewRepository.save(review);
        Movie movie = movieRepository.findById(review.getMovie().getMovieId()).get();

        movie.setReviewCount(movie.getReviewCount()+1);
        movie.setAverageRating(setAverageRatingOfMovie(movie));
        movieRepository.save(movie);

        return new ResponseEntity<>("Review created!", HttpStatus.OK);
    }


    public ResponseEntity<List<Review>> getReviewsByUserId(int userId) {

        List<Review> reviewList = reviewRepository.findAll();
        List<Review> reviewsByUser = new LinkedList<>();


        if (reviewList.isEmpty()) {

            return new ResponseEntity<>(reviewsByUser, HttpStatus.BAD_REQUEST);
        }

        for (Review review : reviewList) {

            if (review.getUser().getAccountID() == userId) {

                reviewsByUser.add(review);
            }
        }

        return new ResponseEntity<>(reviewsByUser, HttpStatus.OK);
    }

    public ResponseEntity<List<Review>> getReviewsByMovieId(int movieId) {

        List<Review> reviewList = reviewRepository.findAll();
        List<Review> reviewsByMovies = new LinkedList<>();

        if (reviewList.isEmpty()) {

            return new ResponseEntity<>(reviewList, HttpStatus.BAD_REQUEST);
        }

        for (Review review : reviewList) {

            if (review.getMovie().getMovieId() == movieId) {

                reviewsByMovies.add(review);
            }
        }

        return new ResponseEntity<>(reviewsByMovies, HttpStatus.OK);
    }

    public ResponseEntity<Review> getReviewById(int reviewId) {
        return new ResponseEntity<>(reviewRepository.findById(reviewId).get(), HttpStatus.OK);
    }

    public Double setAverageRatingOfMovie(Movie movie) {


       // List<Review> reviewsForStats = movie.getReviewsForStats();
        double ratingSum = 0;
        double totalCountOfRatings = 0;

        for (Review review : reviewRepository.findAll()) {
            if (review.getMovie().getMovieId() == movie.getMovieId()) {
                ratingSum += review.getRating();
                totalCountOfRatings++;
            }
        }
        double average = ratingSum / totalCountOfRatings;
        average = Math.round(average * 10.0) / 10.0;

        return average;
    }


    public ResponseEntity<List<Review>> getReviewsByDate(int userId, String firstDate, String lastDate) {

        List<Review> reviews = new LinkedList<>();

        LocalDate first = LocalDate.parse(firstDate);
        LocalDate last = LocalDate.parse(lastDate);
        LocalDate reviewAdded;

        for (Review review : reviewRepository.findAll()) {
            reviewAdded = LocalDate.parse(review.getDateAdded());
            if ((first.isBefore(reviewAdded) || first.isEqual(reviewAdded)) && (last.isEqual(reviewAdded) || last.isAfter(reviewAdded))
                    && review.getUser().getAccountID() == userId) {
                reviews.add(review);
            }
        }

        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }


    public ResponseEntity<Integer> getAmountOfRating(int movieId, int rating){

        int count = 0;

        for(Review review:reviewRepository.findAll()){
            if(review.getMovie().getMovieId() == movieId && review.getRating() == rating){
               count++;
            }
        }
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}

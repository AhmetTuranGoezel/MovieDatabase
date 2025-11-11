package com.frontend.model;

import java.time.LocalDate;

public class Review {

    private int reviewId;
    private int rating;
    private String comment;
    private User user;
    private Movie movie;
    private String movietitle;
    private String dateAdded;

    public Review() {
        this.dateAdded = LocalDate.now().toString();
    }

    public Review(int rating, String comment, User user, Movie movie) {
        this.rating = rating;
        this.comment = comment;
        this.user = user;
        this.movie = movie;
        this.dateAdded = LocalDate.now().toString();
    }

    public Review(int reviewId, int rating, String comment, User user, Movie movie) {
        this.reviewId = reviewId;
        this.rating = rating;
        this.comment = comment;
        this.user = user;
        this.movie = movie;
        this.dateAdded = LocalDate.now().toString();
    }

    public String getMovietitle() {
        return movietitle;
    }

    public void setMovietitle(String movietitle) {
        this.movietitle = movietitle;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded() {
        this.dateAdded = LocalDate.now().toString();
    }
}
package com.backend.model;

import javax.persistence.*;

@Entity
@Table(name = "reviews")
public class Review {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int reviewId;
private int rating;
private String comment;
private String dateAdded;



@ManyToOne
@JoinColumn(name="account_id",referencedColumnName = "account_id")
private User user;

    @ManyToOne
    @JoinColumn(name="movie_id",referencedColumnName = "movie_id")
    private Movie movie;

    public Review() {}


    public Review(int rating, String comment) {
        this.rating = rating;
        this.comment = comment;

    }

    public Review(int reviewId, int rating, String comment) {
        this.reviewId=reviewId;
        this.rating = rating;
        this.comment = comment;


    }

    public Review(int rating, String comment, User user, Movie movie) {
        this.rating = rating;
        this.comment = comment;
        this.user = user;
        this.movie = movie;
    }


    public Review(int reviewId, int rating, String comment, User user, Movie movie) {
        this.reviewId = reviewId;
        this.rating = rating;
        this.comment = comment;
        this.user = user;
        this.movie = movie;
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

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }
}



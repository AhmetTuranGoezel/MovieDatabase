package com.frontend.model;


import java.time.LocalDate;

public class HistoryMovie{

    private int historyMovieId;
    private Movie movie;
    private String dateAdded;


    public HistoryMovie() {
        this.dateAdded = LocalDate.now().toString();
    }

    public HistoryMovie(Movie movie) {
        this.movie=movie;
        this.dateAdded = LocalDate.now().toString();
    }

    public HistoryMovie(int historyMovieId, Movie movie, String date) {
        this.historyMovieId = historyMovieId;
        this.movie = movie;
        this.dateAdded = date;
    }

    public int getHistoryMovieId() {
        return historyMovieId;
    }

    public void setHistoryMovieId(int historyMovieId) {
        this.historyMovieId = historyMovieId;
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

    public void setDate() {
        this.dateAdded = LocalDate.now().toString();
    }
}

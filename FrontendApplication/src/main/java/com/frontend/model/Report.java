package com.frontend.model;

public class Report {

    private int reportId;

    private String text;

    private User user;
    private Movie movie;

    private String username;
    private String userEmail;
    private String movieTitle;
    private int movieId;

    public Report() {

    }

    public Report(String text, User user, Movie movie) {
        this.text = text;
        this.user = user;
        this.movie = movie;
        this.username = user.getUsername();
        this.userEmail = user.getEmail();
        this.movieTitle = movie.getTitle();
        this.movieId = movie.getId();
    }

    public Report(int reportId, String text, User user, Movie movie) {
        this.reportId = reportId;
        this.text = text;
        this.user = user;
        this.movie = movie;
        this.username = user.getUsername();
        this.userEmail = user.getEmail();
        this.movieTitle = movie.getTitle();
        this.movieId = movie.getId();
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(User user) {

        this.username = user.getUsername();
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(User user) {
        this.userEmail = user.getEmail();
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(Movie movie) {
        this.movieTitle = movie.getTitle();
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(Movie movie) {
        this.movieId = movie.getId();
    }
}

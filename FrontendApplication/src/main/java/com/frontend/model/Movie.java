package com.frontend.model;


public class Movie {

    private int movieId;
    private String title = "";
    private String releaseDate = "";
    private String category = "";
    private String movieLength;
    private String director;
    private String writer;
    private String cast = "";
    private String cover;

    private int views;
    private int reviewCount;
    private double averageRating;


    public Movie() {

    }

    public Movie(String title, String releaseDate, String category, String movieLength, String director, String writer, String cast, String cover) {

        this.title = title;
        this.releaseDate = releaseDate;
        this.category = category;
        this.movieLength = movieLength;
        this.director = director;
        this.writer = writer;
        this.cast = cast;
        this.cover = cover;

    }

    public Movie(int id, String title, String releaseDate, String category, String movieLength, String director, String writer, String cast, String cover) {
        this.movieId = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.category = category;
        this.movieLength = movieLength;
        this.director = director;
        this.writer = writer;
        this.cast = cast;
        this.cover = cover;

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getMovieLength() {
        return movieLength;
    }

    public void setMovieLength(String movieLength) {
        this.movieLength = movieLength;
    }

    public int getId() {
        return movieId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getViews() {
        return views;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public double getAverageRating() {
        return averageRating;
    }
}

package com.backend.model;

import javax.persistence.*;
import java.util.Base64;

@Entity
@Table(name = "movie")
public class Movie {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private int movieId;
    @Column(name = "title",columnDefinition= "LONGTEXT")
    private String title;
    @Column(name = "category",columnDefinition= "LONGTEXT")
    private String category;
    @Column(name = "release_date",columnDefinition= "LONGTEXT")
    private String releaseDate;
    @Column(name = "movie_length",columnDefinition= "LONGTEXT")
    private String movieLength;
    @Column(name = "director",columnDefinition= "LONGTEXT")
    private String director;
    @Column(name = "writer",columnDefinition= "LONGTEXT")
    private String writer;
    @Column(name = "cast",columnDefinition= "LONGTEXT")
    private String cast;
    @Lob
    @Column(name = "cover")
    private byte[] cover;

    private int views;
    private int reviewCount;
    private double averageRating;

    public Movie() {
    }

    public Movie(int movieId, String title, String category, String releaseDate, String movieLength, String director, String writer, String cast) {
    }

    public Movie(int movieId, String title, String category, String releaseDate, String movieLength , String director, String writer, String cast, byte[] cover) {
        this.movieId = movieId;
        this.title = title;
        this.category = category;
        this.releaseDate = releaseDate;
        this.movieLength = movieLength;
        this.director = director;
        this.writer = writer;
        this.cast = cast;
        this.cover=cover;
    }

    public Movie(String title, String category, String releaseDate, String movieLength , String director, String writer, String cast, byte[] cover) {

        this.title = title;
        this.category = category;
        this.releaseDate = releaseDate;
        this.movieLength = movieLength;
        this.director = director;
        this.writer = writer;
        this.cast = cast;
        this.cover=cover;
    }

    public void StringToByte(String tempCover){

        this.cover= Base64.getDecoder().decode(tempCover);

    }


    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {return category;}

    public void setCategory(String categoryEnum) {
        this.category = categoryEnum;
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

    public String getMovieLength() {return movieLength;}

    public void setMovieLength(String movieLength) {this.movieLength = movieLength;}


    public byte[] getCover() {
        return cover;
    }

    public void setCover(byte[] cover) {
        this.cover = cover;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
}

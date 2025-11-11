package com.backend.model;

import javax.persistence.*;

@Entity
@Table(name = "historymovies")
public class HistoryMovie{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int historyMovieId;
    private String dateAdded;
    @ManyToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "movie_id")
    private Movie movie;



    public HistoryMovie() {

    }

    public int getHistoryMovieId() {
        return historyMovieId;
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

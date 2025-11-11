package com.backend.model;

import javax.persistence.*;

@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reportId;

    private String text;

    @ManyToOne
    @JoinColumn(name="account_id",referencedColumnName = "account_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="movie_id",referencedColumnName = "movie_id")
    private Movie movie;


    public Report() {
    }

    public Report(String text, User user, Movie movie) {
        this.text = text;
        this.user = user;
        this.movie = movie;
    }

    public Report(int reportId, String text, User user, Movie movie) {
        this.reportId = reportId;
        this.text = text;
        this.user = user;
        this.movie = movie;
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


}

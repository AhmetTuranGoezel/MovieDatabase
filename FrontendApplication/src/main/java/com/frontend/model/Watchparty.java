package com.frontend.model;

public class Watchparty {

    private int watchpartyId;
    private Movie movie;
    private User guest;
    private User host;
    private String date;
    private String time;

    public Watchparty() {
    }

    public Watchparty(Movie movie, User guest, User host, String date, String time) {
        this.movie = movie;
        this.guest = guest;
        this.host = host;
        this.date = date;
        this.time = time;
    }

    public Watchparty(int watchpartyId, Movie movie, User guest, User host, String date, String time) {
        this.watchpartyId = watchpartyId;
        this.movie = movie;
        this.guest = guest;
        this.host = host;
        this.date = date;
        this.time = time;
    }

    public int getWatchpartyId() {
        return watchpartyId;
    }

    public void setWatchpartyId(int watchpartyId) {
        this.watchpartyId = watchpartyId;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public User getGuest() {
        return guest;
    }

    public void setGuest(User guest) {
        this.guest = guest;
    }

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

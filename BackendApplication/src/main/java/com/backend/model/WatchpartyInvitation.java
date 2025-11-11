package com.backend.model;

import javax.persistence.*;

@Entity
public class WatchpartyInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int watchpartyInvitationId;

    @ManyToOne
    @JoinColumn(name="movie_id",referencedColumnName = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name="sender_id",referencedColumnName = "account_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name="recipient_id",referencedColumnName = "account_id")
    private User recipient;

    private String date;

    private String time;

    private String text;


    public WatchpartyInvitation() {
    }

    public WatchpartyInvitation(Movie movie, User sender, User recipient, String date, String time, String text) {
        this.movie = movie;
        this.sender = sender;
        this.recipient = recipient;
        this.date = date;
        this.time = time;
        this.text = text;
    }

    public WatchpartyInvitation(int watchpartyInvitationId, Movie movie, User sender, User recipient, String date, String time, String text) {
        this.watchpartyInvitationId = watchpartyInvitationId;
        this.movie = movie;
        this.sender = sender;
        this.recipient = recipient;
        this.date = date;
        this.time = time;
        this.text = text;
    }

    public int getWatchpartyInvitationId() {
        return watchpartyInvitationId;
    }

    public void setWatchpartyInvitationId(int watchpartyInvitationId) {
        this.watchpartyInvitationId = watchpartyInvitationId;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

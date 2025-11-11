package com.backend.model;


import javax.persistence.*;

@Entity
@Table(name ="notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int notificationId;
    private String text;
    private String type;
    @Lob
    private byte[] notificationImg;

    @ManyToOne
    @JoinColumn(name = "notificated_user", referencedColumnName = "account_id")
    private User notificatedUser;

    @ManyToOne
    @JoinColumn(name = "movie", referencedColumnName = "movie_id")
    private Movie movie;

    public Notification() {
    }

    public Notification(String text, String type, User notificatedUser) {
        this.text = text;
        this.type = type;
        this.notificatedUser = notificatedUser;
    }

    public Notification(int notificationId, String text, String type, User notificatedUser) {
        this.notificationId = notificationId;
        this.text = text;
        this.type = type;
        this.notificatedUser = notificatedUser;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public User getNotificatedUser() {
        return notificatedUser;
    }

    public void setNotificatedUser(User notificatedUser) {
        this.notificatedUser = notificatedUser;
    }

    public byte[] getNotificationImg() {
        return notificationImg;
    }

    public void setNotificationImg(byte[] notificationImg) {
        this.notificationImg = notificationImg;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}

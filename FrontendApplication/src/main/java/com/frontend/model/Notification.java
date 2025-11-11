package com.frontend.model;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.Base64;

public class Notification {

    private int notificationId;
    private String text;
    private String type;
    private User notificatedUser;
    private String notificationImg;
    private Movie movie;

    public enum Type{
        FriendRequest,
        FriendRequestAccepted,
        WatchpartyInv,
        WatchpartyAccepted,
        Report,
    }

    public Notification() {
    }
    
    public Notification(String text, Type type,String notificationImg) {
        this.text = text;
        this.notificationImg=notificationImg;
        this.type = type.toString();
    }

    public Notification(int notificationId, String text, Type type,String notificationImg) {
        this.notificationId = notificationId;
        this.text = text;
        this.type = type.toString();
        this.notificationImg=notificationImg;
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

    public void setText(User user, Type type, Movie movie) {

        switch (type.toString()){
            case "FriendRequest":
                this.text = user.getUsername() + " wants to be your friend!";
                break;

            case "FriendRequestAccepted":
                this.text = user.getUsername() + " accepted your friendrequest";
                break;

            case "WatchpartyInv":
                this.text = user.getUsername() + " wants to watch " + movie.getTitle() + " with you";
                break;
            case "WatchpartyAccepted":
                this.text = user.getUsername() + " accepted your Watchparty invitation for " + movie.getTitle();
                break;
            case "Report":
                this.text = "Your report was successfully processed for " + movie.getTitle();
                break;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type.toString();
    }

    public User getNotificatedUser() {
        return notificatedUser;
    }

    public void setNotificatedUser(User user) {
        this.notificatedUser = user;
    }

    public String getNotificationImg() {
        return notificationImg;
    }

    public void setNotificationImg(Type type) throws IOException {

        switch (type.toString()){
            case "FriendRequest":
            case "FriendRequestAccepted":

                byte[] byteArray = IOUtils.toByteArray(getClass().getResourceAsStream("/sendFR.png"));
                this.notificationImg = Base64.getEncoder().encodeToString(byteArray);
                break;
                
            case "WatchpartyInv":
            case "WatchpartyAccepted":

                byte[] byteArray2 = IOUtils.toByteArray(getClass().getResourceAsStream("/watchparty.png"));
                this.notificationImg = Base64.getEncoder().encodeToString(byteArray2);
                break;

            case "Report":

                byte[] byteArray3 = IOUtils.toByteArray(getClass().getResourceAsStream("/error.jpg"));
                this.notificationImg = Base64.getEncoder().encodeToString(byteArray3);
                break;

        }
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}

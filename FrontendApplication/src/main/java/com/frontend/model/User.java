package com.frontend.model;

import java.util.List;

public class User extends Account {


    private String username;
    private String dateOfBirth;
    private String profileImg;
    private boolean isOnline;

    private List<Movie> watchlist;
    private List<HistoryMovie> history;
    private List<User> friends;
    private List<User> friendrequests;

    private int privacyWl = 1;
    private int privacyH = 1;
    private int privacyFr = 1;
    private int privacyRe = 1;


    public User() {
        setAdmin(false);
    }

    public User(String forename, String surname, String email, long password, String username, String dateOfBirth, String profileImg, String isOnline) {
        super(forename, surname, email, password);
        this.username = username;
        this.dateOfBirth = dateOfBirth;
        this.isOnline = Boolean.parseBoolean(isOnline);
        setAdmin(false);
    }


    public User(int accountID, String forename, String surname, String email, long password, String username, String dateOfBirth, String profileImg, String isOnline) {
        super(accountID, forename, surname, email, password);
        this.username = username;
        this.dateOfBirth = dateOfBirth;
        this.profileImg = profileImg;
        this.isOnline = Boolean.parseBoolean(isOnline);
        setAdmin(false);
    }

    public int getPrivacyWl() {
        return privacyWl;
    }

    public int getPrivacyH() {
        return privacyH;
    }

    public int getPrivacyFr() {
        return privacyFr;
    }

    public int getPrivacyRe() {
        return privacyRe;
    }

    public void setPrivacyWl(int privacyWl) {
        this.privacyWl = privacyWl;
    }

    public void setPrivacyH(int privacyH) {
        this.privacyH = privacyH;
    }

    public void setPrivacyFr(int privacyFr) {
        this.privacyFr = privacyFr;
    }

    public void setPrivacyRe(int privacyRe) {
        this.privacyRe = privacyRe;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        this.isOnline = online;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String img) {
        this.profileImg = img;
    }

    public List<Movie> getWatchlist() {
        return watchlist;
    }

    public void setWatchlist(List<Movie> watchlist) {
        this.watchlist = watchlist;
    }

    public List<HistoryMovie> getHistory() {
        return history;
    }

    public void setHistory(List<HistoryMovie> history) {
        this.history = history;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public List<User> getFriendrequests() {
        return friendrequests;
    }

    public void setFriendrequests(List<User> friendrequests) {
        this.friendrequests = friendrequests;
    }

}


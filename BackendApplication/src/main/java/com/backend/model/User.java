package com.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name ="users")
public class User extends Account {


    @Column(name="username",columnDefinition= "LONGTEXT")
    private String username;
    @Column(name="dateOfBirth",columnDefinition= "LONGTEXT")
    private String dateOfBirth;
    @Lob
    @Column(name = "profileimage")
    private byte[] profileImg;


    @Column(name="isOnline",columnDefinition= "LONGTEXT")
    private String isOnline;

    @ManyToMany
    @JoinTable(
            name = "watchlist",
            joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "movie_id")
    )
    @JsonIgnoreProperties("watchlist")
     private Set<Movie> watchlist;

    @ManyToMany()
    @JoinTable(
            name = "history",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "history_movie_id")
    )
    @JsonIgnoreProperties("history")
    private Set<HistoryMovie> history;


    @ManyToMany
    @JoinTable(
            name = "friends",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id", referencedColumnName = "account_id")
    )
    @JsonIgnoreProperties("friends")
    private Set<User> friends;

    @ManyToMany
    @JoinTable(
            name = "friendrequests",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "account_id")
            // = @JoinColumn(name = "requester_id", referencedColumnName = "account_id")
    )
    @JsonIgnoreProperties(value = "friendrequests",allowSetters = true)
    private Set<User> friendrequests;





    private int privacyWl;
    private int privacyH;
    private int privacyFr;
    private int privacyRe;



    public User() {

    }

        public User(String forename, String surname, String email, long password, String username, String dateOfBirth, byte[] profileimage, String isOnline) {
        super(forename, surname, email, password);
        this.username = username;
        this.dateOfBirth=dateOfBirth;
        this.profileImg=profileimage;
        this.isOnline=isOnline;
    }

    public User(String forename, String surname, String email, long password, String username, String dateOfBirth, byte[] profileimage) {
        super(forename, surname, email, password);
        this.username = username;
        this.dateOfBirth=dateOfBirth;
        this.profileImg=profileimage;
    }

    public User(int accountID, String forename, String surname, String email, long password, String username, String dateOfBirth, byte[] profileimage, String isOnline) {
        super(accountID,forename, surname, email, password);
        this.username = username;
        this.dateOfBirth=dateOfBirth;
        this.profileImg=profileimage;
        this.isOnline=isOnline;
    }


    public User(int accountID, String forename, String surname, String email, long password, String username, String dateOfBirth, byte[] profileImg, String isOnline, int privacyWl, int privacyH, int privacyFr, int privacyRe) {
        super(accountID, forename, surname, email, password);
        this.username = username;
        this.dateOfBirth = dateOfBirth;
        this.profileImg = profileImg;
        this.isOnline = isOnline;
        this.privacyWl = privacyWl;
        this.privacyH = privacyH;
        this.privacyFr = privacyFr;
        this.privacyRe = privacyRe;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public byte[] getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(byte[] profileImg) {
        this.profileImg = profileImg;
    }

    public Set<Movie> getWatchlist() {
        return watchlist;
    }

    public void setWatchlist(Set<Movie> watchlist) { this.watchlist = watchlist;
    }

    public Set<HistoryMovie> getHistory() {
        return history;
    }

    public void setHistory(Set<HistoryMovie> history) {
        this.history = history;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }

    public Set<User> getFriendrequests() {
        return friendrequests;
    }

    public void setFriendrequests(Set<User> friendrequests) {
        this.friendrequests = friendrequests;
    }


    public int getPrivacyWl() {
        return privacyWl;
    }

    public void setPrivacyWl(int privacyWl) {
        this.privacyWl = privacyWl;
    }

    public int getPrivacyH() {
        return privacyH;
    }

    public void setPrivacyH(int privacyH) {
        this.privacyH = privacyH;
    }

    public int getPrivacyFr() {
        return privacyFr;
    }

    public void setPrivacyFr(int privacyFr) {
        this.privacyFr = privacyFr;
    }

    public int getPrivacyRe() {
        return privacyRe;
    }

    public void setPrivacyRe(int privacyRe) {
        this.privacyRe = privacyRe;
    }


}



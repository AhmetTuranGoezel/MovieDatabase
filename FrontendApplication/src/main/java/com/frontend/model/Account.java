package com.frontend.model;

public abstract class Account {

    private int accountID;
    private String forename;
    private String surname;
    private String email;
    private long password;
    private boolean isAdmin;


    public Account() {
    }

    public Account(String forename, String surname, String email, long password) {

        this.forename = forename;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public Account(int accountID, String forename, String surname, String email, long password) {
        this.accountID = accountID;
        this.forename = forename;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int id) {
        this.accountID = id;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getPassword() {
        return password;
    }

    public void setPassword(long password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

}

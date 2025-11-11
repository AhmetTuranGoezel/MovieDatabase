package com.backend.model;

import javax.persistence.*;


@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "account_id")
    private int accountID;
    @Column(name = "forename",columnDefinition= "LONGTEXT")
    private String forename;
    @Column(name = "surname",columnDefinition= "LONGTEXT")
    private String surname;
    @Column(name = "email",columnDefinition= "LONGTEXT")
    private String email;
    @Column(name = "password",columnDefinition= "LONGTEXT")
    private long password;


    public Account() {
    }

    public Account(String forename, String surname, String email, long password) {
        this.forename = forename;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public Account(int accountID,String forename, String surname, String email, long password) {
        this.accountID= accountID;
        this.forename = forename;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountId(int id) {
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



    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }
}

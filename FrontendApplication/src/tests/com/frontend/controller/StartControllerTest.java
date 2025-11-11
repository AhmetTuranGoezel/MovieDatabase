package com.frontend.controller;

import com.frontend.model.Movie;
import com.frontend.model.User;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class StartControllerTest {


    @org.junit.jupiter.api.Test
    void testValidate() throws IOException, InterruptedException {
        StartController startController = new StartController();
        User user = new User();
        user.setAccountID(2);
        User user2 = new User();
        user2.setAccountID(3);
        //System.out.println(user2.getAccountID());
        Movie movie = new Movie();
        movie.setCategory("Horror");
        assertTrue(startController.validate(movie, user));
        assertFalse(startController.validate(movie, user2));
        //System.out.println(user2.getAccountID());
        movie.setCategory("Animation,Thriller,Crime");
        assertFalse(startController.validate(movie, user));
        assertFalse(startController.validate(movie, user2));
        movie.setCategory("Animation,Comedy");
        assertFalse(startController.validate(movie, user));
        assertTrue(startController.validate(movie, user2));
    }

    @org.junit.jupiter.api.Test
    void testCheckGenre() {
        StartController startController = new StartController();
        assertTrue(startController.checkGenre("Horror,Action,Adventure","Animation,Horror,Action"));
        assertFalse(startController.checkGenre("Comedy,Animation","Action,Horror"));
        assertFalse(startController.checkGenre("Comedy,Animation,Adventure","Comedy,Horror,Action"));
    }

    @org.junit.jupiter.api.Test
    void testRecommend() throws IOException, InterruptedException {
        StartController startController = new StartController();
        User user = new User();
        user.setAccountID(2);
        User user2 = new User();
        user2.setAccountID(3);
        Movie movie = new Movie();
        movie.setCategory("Horror");
        assertTrue(startController.recommend(movie, user2));
        movie.setCategory("Animation,Comedy");
        assertTrue(startController.recommend(movie,user));
        movie.setCategory("Animation,Thriller,Crime");
        assertFalse(startController.recommend(movie, user));
    }
}
package com.backend.controller;



import com.backend.model.HistoryMovie;
import com.backend.model.Movie;
import com.backend.model.User;
import com.backend.service.MovieService;
import com.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    MovieService movieService;


    @PostMapping(value = "/users/create/")
    public ResponseEntity<String> createUserAccount(@RequestBody User user) {

        return userService.createUserAccount(user);



    }


    @PostMapping(value = "/users/login/")
    public ResponseEntity<String> loginUser(@RequestBody User user) {

        return userService.loginUser(user);

    }

    @PutMapping(value = "/users/create/")
    public ResponseEntity<String> updateUser(@RequestBody User user) {

        return userService.updateUser(user);

    }



    @PostMapping(value = "/users/watchlist/{id}")
    public ResponseEntity<String> addToWatchlist(@RequestBody Movie movie, @PathVariable("id") int id) {

        return userService.addToWatchlist(movie,id);
    }

    @GetMapping(value="/users/{id}")
    public ResponseEntity<User> geUsersById(@PathVariable("id") int id) {

        return userService.getUsersById(id);
    }


    @GetMapping(value="/users/watchlist/{id}")
    public ResponseEntity<Set> getWatchlistByUserId(@PathVariable("id") int id) {

        return userService.getWatchlistByUserId(id);
    }

    @DeleteMapping("/movies/delete/watchlist/{id}/{movieId}")
    public ResponseEntity<String> deleteMovieFromWatchlist(@PathVariable int id,@PathVariable int movieId){

        return userService.deleteMovieFromWatchlist(id,movieId);
    }

    @GetMapping(value="/users/history/{id}")
    public ResponseEntity<Set> getHistoryByUserId(@PathVariable("id") int id) {

        return userService.getHistoryByUserId(id);
    }


    @GetMapping(value = "/users/")
    public List getAllUserAccounts() {

        return userService.getAllUserAccounts();

    }


    @PostMapping(value = "/users/history/{id}")
    public ResponseEntity<String> addToHistory(@RequestBody HistoryMovie movie, @PathVariable("id") int id) {

        movieService.createHistoryMovie(movie);
        return userService.addToHistory(movie,id);
    }


    @DeleteMapping("/movies/delete/history/{id}/{movieId}")
    public ResponseEntity<String> deleteMovieFromHistory(@PathVariable("id") int id, @PathVariable("movieId") int movieId){

        return userService.deleteMovieFromHistory(id,movieId);
    }



    @PostMapping(value = "/users/friends/{id}")
    public ResponseEntity<String> addToFriendlist(@RequestBody User user, @PathVariable("id") int id) {

        return userService.addToFriendlist(user,id);
    }

    @PostMapping(value = "/users/friendrequest/{id}")
    public ResponseEntity<String> friendRequest(@RequestBody User user, @PathVariable("id") int id) {

        return userService.friendRequest(user,id);
    }

    @GetMapping(value="/users/friendrequest/{id}")
    public ResponseEntity<Set> getFriendsRequestsByUserId(@PathVariable("id") int id) {

        return userService.getFriendRequestsByUserId(id);
    }

    @DeleteMapping("/users/friendrequest/{ownId}/{friendId}")
    public ResponseEntity<String> deleteFriendRequest(@PathVariable("ownId") int ownId, @PathVariable("friendId") int friendId){

        return userService.deleteFriendRequest(ownId,friendId);
    }
    @GetMapping(value="/users/friends/{id}")
    public ResponseEntity<Set> getFriendsByUserId(@PathVariable("id") int id) {

        return userService.getFriendsByUserId(id);
    }

    @DeleteMapping("/users/delete/friends/{ownId}/{friendId}")
    public ResponseEntity<String> deleteFriendFromList(@PathVariable("ownId") int ownId, @PathVariable("friendId") int friendId){

        return userService.deleteFriendFromList(ownId, friendId);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable("username") String username){
        return userService.getUserByUsername(username);
    }

    @GetMapping("/user/checkFriend/{myId}/{userId}")
        public boolean checkIfWeAreFriends(@PathVariable ("myId") int myId, @PathVariable ("userId") int userId){
            return userService.checkIfWeAreFriends(myId,userId);
    }


    @GetMapping("/user/checkFR/{myId}/{userId}")
    public boolean checkIfISentOrGotFR(@PathVariable ("myId") int myId, @PathVariable ("userId") int userId){
        return userService.checkIfISentOrGotFR(myId,userId);
    }



    @GetMapping("/user/checkEmail/{email}")
    public boolean checkEmailUsed(@PathVariable("email") String email){
        return userService.checkIfEmailUsed(email);
    }

    @GetMapping("/user/checkUsername/{username}")
    public boolean checkUsernameUsed(@PathVariable("username") String username){
        return userService.checkIfUsernameUsed(username);
    }

    @PutMapping(value = "/users/update/{userId}/{isOnline}")
    public ResponseEntity<String> updateOnlineStatus(@PathVariable("userId") int userId,@PathVariable("isOnline") String isOnline) {
        return userService.updateOnlineStatus(userId,isOnline);
    }


}

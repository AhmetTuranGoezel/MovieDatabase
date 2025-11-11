package com.backend.service;


import com.backend.model.HistoryMovie;
import com.backend.model.Movie;
import com.backend.model.Systemadministrator;
import com.backend.model.User;
import com.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    SystemadministratorRepository systemadministratorRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    HistoryMovieRepository historyMovieRepository;


    public ResponseEntity<String> createUserAccount(User userAccount) {

        List<User> userList = userRepository.findAll();

        List<Systemadministrator> systemadministratorList= systemadministratorRepository.findAll();


        for (Systemadministrator systemadministrator : systemadministratorList) {

            if (userAccount.getEmail().equals(systemadministrator.getEmail())) {

                return new ResponseEntity<>("This E-Mail is already in use!", HttpStatus.BAD_REQUEST);

            }

        }

        if (userRepository.count() == 0) {

            userRepository.save(userAccount);

            return new ResponseEntity<>("Account was created successfully!", HttpStatus.OK);
        }



        else {

            for (User user : userList) {

                if (userAccount.getEmail().equals(user.getEmail())) {

                    return new ResponseEntity<>("Account already exists!", HttpStatus.BAD_REQUEST);

                }
            }

            userRepository.save(userAccount);
            return new ResponseEntity<>("Account was created successfully!", HttpStatus.OK);


        }

    }


    public ResponseEntity<String> loginUser(User user) {


        List<User> userList = userRepository.findAll();


        for (User users : userList) {

            if (user.getEmail().equals(users.getEmail())) {

                if (user.getPassword() == users.getPassword()) {

                    Random randomCodeGenerator = new Random();
                    int randomCode = 1000+ randomCodeGenerator.nextInt(9999);
                    emailService.sendCode(user.getEmail(),"2FA-Code","Hello "+ users.getUsername()+",\n\n"+
                            "this is your two-factor authentication code: " +randomCode);

                    return new ResponseEntity<>(Integer.toString(randomCode), HttpStatus.OK);
                } else {

                    return new ResponseEntity<>("Wrong password!", HttpStatus.BAD_REQUEST);

                }
            }
        }

        return new ResponseEntity<>("E-Mail wrong or does not exist!", HttpStatus.BAD_REQUEST);

    }



    public List getAllUserAccounts() {

        return userRepository.findAll();
    }


    public ResponseEntity<String> addToWatchlist(Movie movie, int id) {


        List<User> userList = userRepository.findAll();

        for (User user : userList) {

            if (user.getAccountID() == id) {


                for (Iterator<HistoryMovie> historyIterator = user.getHistory().iterator(); historyIterator.hasNext(); ) {
                    HistoryMovie historyMovie = historyIterator.next();
                    if (historyMovie.getMovie().getMovieId() == movie.getMovieId()) {


                        return new ResponseEntity<>("Movie is already in the history!", HttpStatus.BAD_REQUEST);
                    }

                }

                for (Iterator<Movie> watchListIterator = user.getWatchlist().iterator(); watchListIterator.hasNext(); ) {
                    Movie watchListMovie = watchListIterator.next();
                    if (watchListMovie.getMovieId() == movie.getMovieId()) {


                        return new ResponseEntity<>("Movie is already in the watchlist!", HttpStatus.BAD_REQUEST);
                    }

                }


                user.getWatchlist().add(movie);
                userRepository.save(user);

                return new ResponseEntity<>("Movie added to watchlist!", HttpStatus.OK);

            }

        }
        return new ResponseEntity<>("Account not available. Therefore the movie was not added", HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<User> getUsersById(int id) {
        return new ResponseEntity<>(userRepository.findById(id).get(),HttpStatus.OK);
    }


    public ResponseEntity<Set> getWatchlistByUserId(int id) {
        return new ResponseEntity<>(userRepository.findById(id).get().getWatchlist(),HttpStatus.OK);
    }


    public ResponseEntity<String> deleteMovieFromWatchlist(int id, int movieId) {

        List<User> userList = userRepository.findAll();


        for (User user : userList) {

            if (user.getAccountID() == id) {


                for (Iterator<Movie> watchlistIterator = user.getWatchlist().iterator(); watchlistIterator.hasNext();) {
                    Movie watchlistMovie =  watchlistIterator.next();
                    if (watchlistMovie.getMovieId()==movieId) {
                       watchlistIterator.remove();
                        userRepository.save(user);
                        return new ResponseEntity<>("Movie removed from watchlist!", HttpStatus.OK);
                    }
                }

                }

            }

        return new ResponseEntity<>("Movie not found in watchlist!",HttpStatus.BAD_REQUEST);
        }






    public ResponseEntity<String> addToHistory(HistoryMovie movie, int id) {


        //historyMovieRepository.save(movie);
        /*MovieService ms = new MovieService();
        ms.addMovieToStatsList(movie);*/



        for (User user: userRepository.findAll()) {

            if(user.getAccountID()==id) {

                user.getHistory().add(movie);
                userRepository.save(user);

                return new ResponseEntity<>("Movie added to history!", HttpStatus.OK);
            }

        }
        return new ResponseEntity<>("Account not available. Therefore the movie was not added.",HttpStatus.OK);

    }

    public ResponseEntity<Set> getHistoryByUserId(int id) {

        return new ResponseEntity<>(userRepository.findById(id).get().getHistory(),HttpStatus.OK);
    }


    public ResponseEntity<String> updateUser(User user) {

            userRepository.save(user);
            return new ResponseEntity<>("User updated", HttpStatus.OK);
        }

    public boolean checkIfEmailUsed(String email){

            for (User repoUser : userRepository.findAll()) {
                if (email.equals(repoUser.getEmail())) {
                    return true;
                }
            }
            for (Systemadministrator systemadministrator : systemadministratorRepository.findAll()) {
                if (email.equals(systemadministrator.getEmail())) {
                    return true;
                }
            }
            return false;
        }

    public boolean checkIfUsernameUsed(String username){

        for (User repoUser : userRepository.findAll()) {
            if (username.equals(repoUser.getUsername())) {
                return true;
            }
        }
        return false;
    }



    public ResponseEntity<String> deleteMovieFromHistory(int id,int movieId) {

        List<User> userList = userRepository.findAll();


        for (User user : userList) {

            if (user.getAccountID() == id) {


                for (Iterator<HistoryMovie> historyIterator = user.getHistory().iterator(); historyIterator.hasNext();) {
                    HistoryMovie historyMovie =  historyIterator.next();
                    if (historyMovie.getMovie().getMovieId()==movieId) {
                        historyIterator.remove();
                        userRepository.save(user);
                        return new ResponseEntity<>("Movie removed from history!", HttpStatus.OK);
                    }
                }

            }

        }

        return new ResponseEntity<>("Movie not found in history!",HttpStatus.BAD_REQUEST);


    }

    public ResponseEntity<String> addToFriendlist(User user, int id) {

        List<User> userList = userRepository.findAll();


        for (User users : userList) {

            if (users.getAccountID() == id) {


                if(users.getFriends().isEmpty()) {

                    users.getFriends().add(user);
                    user.getFriends().add(users);
                    userRepository.save(users);
                    userRepository.save(user);

                    return new ResponseEntity<>("Friend added",HttpStatus.OK);
                }

                for (Iterator<User> friendlistIterator = users.getFriends().iterator(); friendlistIterator.hasNext(); ) {
                    User friend = friendlistIterator.next();
                    if (friend.getAccountID()==user.getAccountID()) {



                        return new ResponseEntity<>("You are friends already!", HttpStatus.BAD_REQUEST);
                    }

                    else {
                        users.getFriends().add(user);
                        user.getFriends().add(users);
                        userRepository.save(users);
                        userRepository.save(user);
                        return new ResponseEntity<>("xxx",HttpStatus.OK);
                    }

                }
            }

        }
        return new ResponseEntity<>("User not found!", HttpStatus.BAD_REQUEST);



    }


    public ResponseEntity<String> friendRequest(User user, int id) {

        List<User> userList = userRepository.findAll();


        for (User users : userList) {

            if (users.getAccountID() == id) {

                if(users.getFriendrequests().isEmpty()) {

                    users.getFriendrequests().add(user);
                    userRepository.save(user);

                    return new ResponseEntity<>("Request sent!",HttpStatus.OK);
                }

                for (Iterator<User> friendRequestIterator = users.getFriendrequests().iterator(); friendRequestIterator.hasNext(); ) {
                    User friend = friendRequestIterator.next();
                    if (friend.getAccountID()==user.getAccountID()) {



                        return new ResponseEntity<>("Friend request sent already!", HttpStatus.BAD_REQUEST);
                    }

                    else {
                        users.getFriendrequests().add(user);
                        userRepository.save(user);
                        return new ResponseEntity<>("Request sent!",HttpStatus.OK);
                    }

                }
            }

        }
        return new ResponseEntity<>("User not found!", HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<Set> getFriendsByUserId(int id) {

        return new ResponseEntity<>(userRepository.findById(id).get().getFriends(),HttpStatus.OK);
    }

    public ResponseEntity<Set> getFriendRequestsByUserId(int id) {
        return new ResponseEntity<>(userRepository.findById(id).get().getFriendrequests(),HttpStatus.OK);
    }

    public ResponseEntity<String> deleteFriendRequest(int ownId, int friendId) {
        List<User> userList = userRepository.findAll();


        for (User user : userList) {

            if (user.getAccountID() == ownId) {

                for (Iterator<User> friendsIterator = user.getFriendrequests().iterator(); friendsIterator.hasNext();) {
                    User friendRequest =  friendsIterator.next();
                    if (friendRequest.getAccountID()==friendId) {
                        friendsIterator.remove();
                        userRepository.save(user);
                        return new ResponseEntity<>("Friend request removed!", HttpStatus.OK);
                    }
                }

            }

        }

        return new ResponseEntity<>("Friend request not available!",HttpStatus.BAD_REQUEST);


    }

    public ResponseEntity<String> deleteFriendFromList(int ownId, int friendId) {

        List<User> userList = userRepository.findAll();


        for (User user : userList) {

            if (user.getAccountID() == ownId) {

                for (Iterator<User> friendsIterator = user.getFriends().iterator(); friendsIterator.hasNext();) {
                    User friend =  friendsIterator.next();
                    if (friend.getAccountID()==friendId) {
                        friendsIterator.remove();
                        friend.getFriends().remove(user);
                        userRepository.save(user);
                        userRepository.save(friend);
                        return new ResponseEntity<>("Friend removed!", HttpStatus.OK);
                    }
                }

            }

        }

        return new ResponseEntity<>("Friend not available!",HttpStatus.BAD_REQUEST);



    }


    public ResponseEntity<User> getUserByUsername(String username) {

        for (User user: userRepository.findAll()) {
            if(user.getUsername().equals(username)){

                return new ResponseEntity<>(user,HttpStatus.OK);

            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    public boolean checkIfWeAreFriends (int myId, int userId){

        User me = userRepository.findById(myId).get();
        User otherUser = userRepository.findById(userId).get();

        Set<User> myFriends = me.getFriends();

        if(myFriends.contains(otherUser)){
            return true;
        }else{
            return false;
        }
    }



    public boolean checkIfISentOrGotFR(int myId, int userId){

        User me = userRepository.findById(myId).get();
        User potentialFriend = userRepository.findById(userId).get();

        Set<User> frOfMyPotentialFriend = potentialFriend.getFriendrequests();
        Set<User> myFriendRequests = me.getFriendrequests();

        if(frOfMyPotentialFriend.contains(me) || myFriendRequests.contains(potentialFriend)){
            return true;
        }else{
            return false;
        }
    }

    public ResponseEntity<String> updateOnlineStatus(int userId, String isOnline) {
        if (isOnline.equals("true")) {
            userRepository.findById(userId).get().setIsOnline("true");
            userRepository.save(userRepository.findById(userId).get());
            return new ResponseEntity<>("Online", HttpStatus.OK);
        } else if (isOnline.equals("false")) {
            userRepository.findById(userId).get().setIsOnline("false");
            userRepository.save(userRepository.findById(userId).get());
            return new ResponseEntity<>("Offline", HttpStatus.OK);
        }
        return new ResponseEntity<>("False status!", HttpStatus.BAD_REQUEST);
    }
}


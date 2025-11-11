package com.frontend.controller;

import com.frontend.FrontendApplication;
import com.frontend.model.*;
import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ForeignProfileController extends SceneController implements Initializable {


    @FXML
    private Label labelBirthday;

    @FXML
    private Label labelFirstname;

    @FXML
    private Label labelLastname;

    @FXML
    private Label labelUsername;

    @FXML
    private ImageView profileImg;

    @FXML
    private TableView<Movie> wltable;

    @FXML
    private TableColumn<Movie, String> wltitle;

    @FXML
    private TableView<Movie> htable;

    @FXML
    private TableColumn<Movie, String> htitle;
    @FXML
    private TableView<Review> retable;
    @FXML
    private TableColumn<Review, Integer> rereview;
    @FXML
    private TableColumn<Review, String> retitle;
    @FXML
    private TableView<User> friendsTabel;
    @FXML
    private TableColumn<User, String> friendsun;
    @FXML
    private Button bSendR;
    @FXML
    private Label sentRequest;


    private Requests request = new Requests();
    private Gson gson = new Gson();
    private Notification notification = new Notification();


    public void sendFriendRequest() throws IOException, InterruptedException {

        User me = (User) FrontendApplication.getCurrentAccount();
        request.post("/users/friendrequest/" + FrontendApplication.getSelectedUser().getAccountID(), gson.toJson(me));

        notification.setType(Notification.Type.FriendRequest);
        notification.setText(me, Notification.Type.FriendRequest,null);
        notification.setNotificatedUser(FrontendApplication.getSelectedUser());
        notification.setNotificationImg(Notification.Type.FriendRequest);


        request.post("/notification/create/",gson.toJson(notification));

        request.get("/users/" + FrontendApplication.getCurrentAccount().getAccountID());
        me = gson.fromJson(request.getMessage(), User.class);
        FrontendApplication.setCurrentAccount(me);

        sentRequest.setText("You have successfully sent a friend request to " + FrontendApplication.getSelectedUser().getUsername());

        bSendR.setText("Friendrequest sent");
        bSendR.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            request.get("/users/" + FrontendApplication.getSelectedUser().getAccountID());
            User user = gson.fromJson(request.getMessage(), User.class);

            labelUsername.setText(user.getUsername());
            labelFirstname.setText(user.getForename());
            labelLastname.setText(user.getSurname());
            labelBirthday.setText(user.getDateOfBirth());

            if (user.getProfileImg() != null) {
                byte[] bytes = Base64.getDecoder().decode(user.getProfileImg());
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                Image image = new Image(bais);
                profileImg.setImage(image);
            }

            if (!FrontendApplication.getCurrentAccount().isAdmin()) {
                request.get("/users/friends/" + FrontendApplication.getCurrentAccount().getAccountID());

                String myFriendsAsJson = request.getMessage();
                User[] myFriendsArray = gson.fromJson(myFriendsAsJson, User[].class);
                for (User friend : myFriendsArray) {
                    if (friend.getAccountID() == user.getAccountID()) {
                        bSendR.setVisible(false);
                    }
                }

                request.get("/users/friendrequest/" + FrontendApplication.getCurrentAccount().getAccountID());
                String myFriendrequestsAsJson = request.getMessage();
                User[] myFriendrequestsArray = gson.fromJson(myFriendrequestsAsJson, User[].class);

                for (User friend : myFriendrequestsArray) {
                    if (friend.getAccountID() == user.getAccountID()) {
                        bSendR.setText("Accept friendrequest");
                        bSendR.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                try {
                                    request.post("/users/friends/" + FrontendApplication.getCurrentAccount().getAccountID(), gson.toJson(user));
                                    request.delete("/users/friendrequest/" + FrontendApplication.getCurrentAccount().getAccountID()
                                            + "/" + user.getAccountID());

                                    switchToForeignUserProfile();
                                } catch (IOException | InterruptedException exc) {
                                    exc.printStackTrace();
                                }
                            }
                        });
                    }
                }

                request.get("/users/friendrequest/" + user.getAccountID());
                String friendrequestsAsJson = request.getMessage();
                User[] friendrequestsArray = gson.fromJson(friendrequestsAsJson, User[].class);

                for (User requester : friendrequestsArray) {
                    if (requester.getAccountID() == FrontendApplication.getCurrentAccount().getAccountID()) {
                        bSendR.setText("Friendrequest sent");
                        bSendR.setDisable(true);
                    }
                }
            }


            request.get("/users/watchlist/" + user.getAccountID());
            String moviesAsJson = request.getMessage();

            Movie[] movies = gson.fromJson(moviesAsJson, Movie[].class);
            ArrayList<Movie> tempmovies = new ArrayList<>(Arrays.asList(movies));
            ObservableList<Movie> obsmovies = FXCollections.observableArrayList(tempmovies);
            wltitle.setCellValueFactory(new PropertyValueFactory<Movie, String>("title"));
            wltable.setItems(obsmovies);
            wltable.setVisible(false);

            request.get("/users/history/" + user.getAccountID());
            String historyAsJson = request.getMessage();

            HistoryMovie[] hmovies = gson.fromJson(historyAsJson, HistoryMovie[].class);

            List<Movie> moviesInHistory = new LinkedList<>();

            for(HistoryMovie hm : hmovies){
                moviesInHistory.add(hm.getMovie());
            }

            ObservableList<Movie> obshmovies = FXCollections.observableArrayList(moviesInHistory);
            htitle.setCellValueFactory(new PropertyValueFactory<Movie, String>("title"));
            htable.setItems(obshmovies);
            htable.setVisible(false);

            request.get("/reviews/user/" + user.getAccountID());
            String reviewsAsJson = request.getMessage();

            Review[] reviews = gson.fromJson(reviewsAsJson, Review[].class);
            ArrayList<Review> tempreviews = new ArrayList<>(Arrays.asList(reviews));
            ObservableList<Review> obsreviews = FXCollections.observableArrayList(tempreviews);

            for (Review re : obsreviews) {
                re.setMovietitle(re.getMovie().getTitle());
            }
            retitle.setCellValueFactory(new PropertyValueFactory<Review, String>("movietitle"));
            rereview.setCellValueFactory(new PropertyValueFactory<Review, Integer>("rating"));
            retable.setItems(obsreviews);
            retable.setVisible(false);


            request.get("/users/friends/" + user.getAccountID());
            String friendlistAsJson = request.getMessage();

            User[] friendArray = gson.fromJson(friendlistAsJson, User[].class);
            ArrayList<User> tempfriends = new ArrayList<>(Arrays.asList(friendArray));
            ObservableList<User> obsfriends = FXCollections.observableArrayList(tempfriends);
            friendsun.setCellValueFactory(new PropertyValueFactory<User, String>("Username"));
            friendsTabel.setItems(obsfriends);
            friendsTabel.setVisible(false);


            System.out.println(user.getPrivacyWl());

            String friendsAsJson;

            switch (user.getPrivacyWl()) {
                case 1:
                    wltable.setVisible(true);
                    break;
                case 2:
                    if (!FrontendApplication.getCurrentAccount().isAdmin()) {
                        request.get("/users/friends/" + FrontendApplication.getCurrentAccount().getAccountID());
                        friendsAsJson = request.getMessage();

                        User[] friends = gson.fromJson(friendsAsJson, User[].class);
                        for (User friend : friends) {
                            if (friend.getAccountID() == user.getAccountID()) {
                                wltable.setVisible(true);
                            }
                        }
                    }
                    break;
                case 3:
                    wltable.setVisible(false);
                    break;
            }

            switch (user.getPrivacyH()) {
                case 1:
                    htable.setVisible(true);
                    break;
                case 2:
                    if (!FrontendApplication.getCurrentAccount().isAdmin()) {
                        request.get("/users/friends/" + FrontendApplication.getCurrentAccount().getAccountID());
                        friendsAsJson = request.getMessage();

                        User[] friends = gson.fromJson(friendsAsJson, User[].class);
                        for (User friend : friends) {
                            if (friend.getAccountID() == user.getAccountID()) {
                                htable.setVisible(true);
                            }
                        }
                    }
                    break;
                case 3:
                    htable.setVisible(false);
                    break;
            }
            switch (user.getPrivacyFr()) {
                case 1:
                    friendsTabel.setVisible(true);
                    break;
                case 2:
                    if (!FrontendApplication.getCurrentAccount().isAdmin()) {
                        request.get("/users/friends/" + FrontendApplication.getCurrentAccount().getAccountID());
                        friendsAsJson = request.getMessage();

                        User[] friends = gson.fromJson(friendsAsJson, User[].class);
                        for (User friend : friends) {
                            if (friend.getAccountID() == user.getAccountID()) {
                                friendsTabel.setVisible(true);
                            }
                        }
                    }
                    break;
                case 3:
                    friendsTabel.setVisible(false);
                    break;
            }
            switch (user.getPrivacyRe()) {
                case 1:
                    retable.setVisible(true);
                    break;
                case 2:
                    if (!FrontendApplication.getCurrentAccount().isAdmin()) {
                        request.get("/users/friends/" + FrontendApplication.getCurrentAccount().getAccountID());
                        friendsAsJson = request.getMessage();

                        User[] friends = gson.fromJson(friendsAsJson, User[].class);
                        for (User friend : friends) {
                            if (friend.getAccountID() == user.getAccountID()) {
                                retable.setVisible(true);
                            }
                        }
                    }
                    break;
                case 3:
                    retable.setVisible(false);
                    break;
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}

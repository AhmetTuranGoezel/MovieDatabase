package com.frontend.controller;

import com.frontend.FrontendApplication;
import com.frontend.model.*;
import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.*;

public class ProfileController extends SceneController implements Initializable {
    @FXML
    private Label doblabel;
    @FXML
    private Label fnamelabel;
    @FXML
    private Label lnamelabel;
    @FXML
    private Label unamelabel;
    @FXML
    private ImageView imageview;
    @FXML
    private TableView<Movie> wltable;
    @FXML
    private TableColumn<Movie, String> wltitle;
    @FXML
    private TableView<Movie> htable;
    @FXML
    private TableColumn<Movie, String> htitle;
    @FXML
    private TableView<User> ftable;
    @FXML
    private TableColumn<User, String> fname;
    @FXML
    private TableView<Review> retable;
    @FXML
    private TableColumn<Review, Integer> rereview;
    @FXML
    private TableColumn<Review, String> retitle;
    private Requests request = new Requests();
    private Gson gson = new Gson();
    private User me = (User) FrontendApplication.getCurrentAccount();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        me = (User) FrontendApplication.getCurrentAccount();
        unamelabel.setText(me.getUsername());
        lnamelabel.setText(me.getSurname());
        fnamelabel.setText(me.getForename());
        doblabel.setText(me.getDateOfBirth());
        try {
            request.get("/users/watchlist/" + FrontendApplication.getCurrentAccount().getAccountID());
            String moviesAsJson = request.getMessage();

            Movie[] movies = gson.fromJson(moviesAsJson, Movie[].class);
            ArrayList<Movie> tempmovies = new ArrayList<>(Arrays.asList(movies));
            ObservableList<Movie> obsmovies = FXCollections.observableArrayList(tempmovies);
            wltitle.setCellValueFactory(new PropertyValueFactory<Movie, String>("title"));
            wltable.setItems(obsmovies);

            request.get("/users/history/" + FrontendApplication.getCurrentAccount().getAccountID());
            String historyAsJson = request.getMessage();

            HistoryMovie[] hmovies = gson.fromJson(historyAsJson, HistoryMovie[].class);
            List<Movie> moviesInHistory = new LinkedList<>();

            for(HistoryMovie hm : hmovies){
                moviesInHistory.add(hm.getMovie());
            }

            ObservableList<Movie> obshmovies = FXCollections.observableArrayList(moviesInHistory);
            htitle.setCellValueFactory(new PropertyValueFactory<Movie, String>("title"));
            htable.setItems(obshmovies);

            request.get("/users/friends/" + FrontendApplication.getCurrentAccount().getAccountID());
            String friendsAsJson = request.getMessage();

            User[] friends = gson.fromJson(friendsAsJson, User[].class);
            ArrayList<User> tempfriends = new ArrayList<>(Arrays.asList(friends));
            ObservableList<User> obsfriends = FXCollections.observableArrayList(tempfriends);
            fname.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
            ftable.setItems(obsfriends);

            request.get("/reviews/user/" + me.getAccountID());
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (me.getProfileImg() != null) {
            byte[] bytes = Base64.getDecoder().decode(me.getProfileImg());
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            Image image = new Image(bais);
            imageview.setImage(image);
        }
    }
}

package com.frontend.controller;


import com.frontend.FrontendApplication;
import com.frontend.model.*;
import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class FriendslistController extends SceneController implements Initializable {

    @FXML
    private VBox friendsOverview;

    @FXML
    private VBox notificationOverview;

    @FXML
    private ScrollPane scrollpaneNotifications;
    @FXML
    private TextField tfSearchUser;
    @FXML
    private Label noResult;
    @FXML
    private TableView<User> tableFR;
    @FXML
    private TableColumn<User, String> columnUsernameFR;

    @FXML
    private Button addPerson;

    @FXML
    private VBox overviewFR;

    @FXML
    private VBox overviewG;

    @FXML
    private VBox overviewWatchparty;

    private Requests request = new Requests();
    private Gson gson = new Gson();


    public void searchUser() throws IOException, InterruptedException {

        User me = (User) FrontendApplication.getCurrentAccount();

        if (!tfSearchUser.getText().isEmpty()) {
            if (tfSearchUser.getText().equalsIgnoreCase(me.getUsername())) {
                switchToOwnProfile();
            } else {

                request.get("/users/");
                String usersAsJson = request.getMessage();
                User[] userArray = gson.fromJson(usersAsJson, User[].class);

                for (User u : userArray) {
                    if (tfSearchUser.getText().equalsIgnoreCase(u.getUsername())) {
                        FrontendApplication.setSelectedUser(u);
                        switchToForeignUserProfile();
                    } else {
                        noResult.setText("No result found. ");
                    }
                }
            }
        } else if (tableFR.getSelectionModel().getSelectedItem() != null) {
            switchToForeignUserProfile();
        }
    }

    public void showOrHideNotifications() throws IOException, InterruptedException{

        if(notificationOverview.isVisible()){
            notificationOverview.setVisible(false);
            scrollpaneNotifications.setVisible(false);
        }else {

            notificationOverview.getChildren().clear();
            request.get("/notifications/" + FrontendApplication.getCurrentAccount().getAccountID());
            Notification[] notifications = gson.fromJson(request.getMessage(), Notification[].class);


            for (Notification n : notifications) {

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/views/notificationItem.fxml"));
                HBox hBox = fxmlLoader.load();
                NotificationItemController controller = fxmlLoader.getController();
                controller.setNotification(n);
                notificationOverview.getChildren().add(hBox);


                notificationOverview.setVisible(true);
                scrollpaneNotifications.setVisible(true);
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            request.get("/users/friends/" + FrontendApplication.getCurrentAccount().getAccountID());
            String usersAsJson = request.getMessage();
            User[] userArray = gson.fromJson(usersAsJson, User[].class);

            for (User friend : userArray) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/views/friendslistItem.fxml"));
                HBox hBox = fxmlLoader.load();
                FriendslistItemController friendslistItemController = fxmlLoader.getController();
                friendslistItemController.updateData(friend);
                friendsOverview.getChildren().add(hBox);

            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        try {
            request.get("/users/friendrequest/" + FrontendApplication.getCurrentAccount().getAccountID());
            String friendrequestsAsJson = request.getMessage();
            User[] friendrequests = gson.fromJson(friendrequestsAsJson, User[].class);

            for (User friend : friendrequests) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/views/friendsrequestItem.fxml"));
                HBox hBox = fxmlLoader.load();
                FriendsrequestItemController friendsrequestItemController = fxmlLoader.getController();
                friendsrequestItemController.setFriendrequest(friend);
                overviewFR.getChildren().add(hBox);

            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        try {
             request.get("/watchpartys/" + FrontendApplication.getCurrentAccount().getAccountID());
            String watchpartysAsJson = request.getMessage();
            Watchparty[] watchpartys = gson.fromJson(watchpartysAsJson, Watchparty[].class);

            for (Watchparty watchparty : watchpartys) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/views/watchpartyItem.fxml"));
                HBox hBox = fxmlLoader.load();
                WatchpartyItemController watchpartyItemController = fxmlLoader.getController();
                watchpartyItemController.setWatchparty(watchparty);
                overviewWatchparty.getChildren().add(hBox);

            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        try {
            request.get("/user/discussiongroups/" +  FrontendApplication.getCurrentAccount().getAccountID());
            String discussionGrouprequestsAsJson = request.getMessage();
            DiscussionGroup[] grouprequests = gson.fromJson(discussionGrouprequestsAsJson, DiscussionGroup[].class);

            for (DiscussionGroup discussionGroup : grouprequests) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/views/myGroupsItem.fxml"));
                HBox hBox = fxmlLoader.load();
                GroupItemController groupItemController = fxmlLoader.getController();
                groupItemController.setGroup(discussionGroup);
                overviewG.getChildren().add(hBox);

            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }

    }
}

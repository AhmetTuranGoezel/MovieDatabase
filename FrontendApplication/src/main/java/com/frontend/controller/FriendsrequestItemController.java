package com.frontend.controller;

import com.frontend.FrontendApplication;
import com.frontend.model.Notification;
import com.frontend.model.Requests;
import com.frontend.model.User;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class FriendsrequestItemController extends SceneController{

    @FXML
    private ImageView userImg;

    @FXML
    private Hyperlink userN;

    private Requests request = new Requests();
    private Gson gson = new Gson();
    private User me = new User();
    private User friend = new User();
    private Notification notification = new Notification();

    public void setFriendrequest(User friend) throws IOException, InterruptedException {

        this.friend=friend;
        userN.setText(friend.getUsername());

        if (friend.getProfileImg() != null) {
            byte[] bytes = Base64.getDecoder().decode(friend.getProfileImg());
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            Image image = new Image(bais);
            userImg.setImage(image);

        }

    }

    public void acceptFR() throws IOException, InterruptedException {
        me = (User) FrontendApplication.getCurrentAccount();
        request.post("/users/friends/" + me.getAccountID(), gson.toJson(friend));
        request.delete("/users/friendrequest/" + me.getAccountID()
                + "/" + friend.getAccountID());

        notification.setType(Notification.Type.FriendRequestAccepted);
        notification.setText(me, Notification.Type.FriendRequestAccepted,null);
        notification.setNotificatedUser(friend);
        notification.setNotificationImg(Notification.Type.FriendRequestAccepted);

        request.post("/notification/create/",gson.toJson(notification));


        request.get("/users/" + FrontendApplication.getCurrentAccount().getAccountID());
        User user = gson.fromJson(request.getMessage(), User.class);
        FrontendApplication.setCurrentAccount(user);
        switchToSocial();
    }

    public void reject() throws IOException, InterruptedException {

        request.delete("/users/friendrequest/" + FrontendApplication.getCurrentAccount().getAccountID()
                + "/" + friend.getAccountID());
        request.get("/users/" + FrontendApplication.getCurrentAccount().getAccountID());
        User user = gson.fromJson(request.getMessage(), User.class);
        FrontendApplication.setCurrentAccount(user);
        switchToSocial();
    }

    public void goToProfile() throws IOException, InterruptedException {

        FrontendApplication.setSelectedUser(friend);
        switchToForeignUserProfile();
    }
}

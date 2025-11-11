package com.frontend.controller;

import com.frontend.FrontendApplication;
import com.frontend.model.Notification;
import com.frontend.model.Requests;
import com.frontend.model.User;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class DiscussionGroupMemberItemController extends SceneController {

    @FXML
    private Hyperlink userName;
    @FXML
    private Button buttonAddFriend;
    @FXML
    private Label firstName;

    @FXML
    private Label lastName;

    @FXML
    private ImageView img;

    @FXML
    private ImageView onlineStatus;

    private Requests request = new Requests();

    private Gson gson = new Gson();

    private User participant = new User();
    private  User me = new User();
    private Notification notification = new Notification();


    public void setParticipant(User participant) throws IOException, InterruptedException {

        this.participant = participant;

        userName.setText(participant.getUsername());
        firstName.setText(participant.getForename());
        lastName.setText(participant.getSurname());

        if (participant.getProfileImg() != null) {
            byte[] bytes = Base64.getDecoder().decode(participant.getProfileImg());
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            Image image = new Image(bais);
            img.setImage(image);

        }

        me = (User) FrontendApplication.getCurrentAccount();

        if (me.getAccountID() == participant.getAccountID()) {
            buttonAddFriend.setVisible(false);
        }else {
            request.get("/user/checkFriend/" + FrontendApplication.getCurrentAccount().getAccountID() + "/" + participant.getAccountID());

            if (request.getMessage().equals("true")) {
                buttonAddFriend.setVisible(false);
            } else {
                request.get("/user/checkFR/" + FrontendApplication.getCurrentAccount().getAccountID() + "/" + participant.getAccountID());

                if (request.getMessage().equals("true")) {
                    buttonAddFriend.setDisable(true);
                } else {
                    buttonAddFriend.setDisable(false);
                    buttonAddFriend.setVisible(true);
                }
            }
        }


        Image online = new Image(getClass().getResourceAsStream("/green.png"));

        if (participant.isOnline()) {
            onlineStatus.setImage(online);
        } else {
            onlineStatus.setImage(null);
        }
    }

    public void goToProfile() throws IOException, InterruptedException {

        if (FrontendApplication.getCurrentAccount().getAccountID() == participant.getAccountID()) {
            switchToOwnProfile();
        } else {
            FrontendApplication.setSelectedUser(participant);
            switchToForeignUserProfile();
        }
    }

    public void addFriend() throws IOException, InterruptedException {

        request.post("/users/friendrequest/" + participant.getAccountID(), gson.toJson(me));

        notification.setType(Notification.Type.FriendRequest);
        notification.setText(me, Notification.Type.FriendRequest,null);
        notification.setNotificatedUser(participant);
        notification.setNotificationImg(Notification.Type.FriendRequest);

        request.post("/notification/create/",gson.toJson(notification));

        FrontendApplication.updateCurrentAccount();
        buttonAddFriend.setDisable(true);
    }

}

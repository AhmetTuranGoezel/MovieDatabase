package com.frontend.controller;

import com.frontend.FrontendApplication;
import com.frontend.model.Requests;
import com.frontend.model.User;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class FriendslistItemController extends SceneController{

    @FXML
    private ImageView img;
    @FXML
    private Hyperlink userN;
    @FXML
    private Label firstN;
    @FXML
    private Label lastN;
    @FXML
    private ImageView onlineS;
    @FXML
    private Button startC;

    private User friend = new User();


    public void updateData(User friend) throws IOException, InterruptedException {

        this.friend = friend;
        userN.setText(friend.getUsername());
        firstN.setText(friend.getForename());
        lastN.setText(friend.getSurname());


        if (friend.getProfileImg() != null) {
            byte[] bytes = Base64.getDecoder().decode(friend.getProfileImg());
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            Image image = new Image(bais);
            img.setImage(image);

        }


        Image online = new Image(getClass().getResourceAsStream("/green.png"));
        if (friend.isOnline()) {
            onlineS.setImage(online);
            startC.setVisible(true);
            switchToChat();
        } else {
            startC.setVisible(false);
        }
    }

    public void openChat() throws IOException, InterruptedException {

        User user = (User) FrontendApplication.getCurrentAccount();
        FrontendApplication.getCurrentClient().sendMessage("Invite#" + user.getUsername() + "#" + friend.getAccountID());
        switchToChat();
    }

    public void deleteFriend() throws IOException, InterruptedException {
        Requests request = new Requests();
        request.delete("/users/delete/friends/" + FrontendApplication.getCurrentAccount().getAccountID() + "/" + friend.getAccountID());

        Gson gson = new Gson();
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

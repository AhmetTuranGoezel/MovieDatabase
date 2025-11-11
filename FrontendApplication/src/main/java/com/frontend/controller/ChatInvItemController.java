package com.frontend.controller;

import com.frontend.FrontendApplication;
import com.frontend.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class ChatInvItemController {

    @FXML
    private ImageView img;

    @FXML
    private ImageView onlineS;

    @FXML
    private Button startC;

    @FXML
    private Label userN;

    private User invitableUser = new User();

    private boolean groupMember;

    public void updateData(User user) throws IOException, InterruptedException {

        this.invitableUser = user;

        userN.setText(invitableUser.getUsername());

        if (invitableUser.getProfileImg() != null) {
            byte[] bytes = Base64.getDecoder().decode(invitableUser.getProfileImg());
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            Image image = new Image(bais);
            img.setImage(image);

        }


        Image online = new Image(getClass().getResourceAsStream("/green.png"));
        if (invitableUser.isOnline()) {
            onlineS.setImage(online);
            startC.setVisible(true);
        } else {
            startC.setVisible(false);
        }
    }


    public void invite() throws IOException {
        User user = (User) FrontendApplication.getCurrentAccount();
        ChatController chatController = FrontendApplication.getCurrentFxmlLoader().getController();
        if(groupMember){
            FrontendApplication.getCurrentClient().sendMessage("InviteToDiscussionGroup#" + user.getUsername() + "#" + invitableUser.getAccountID()+"#"+FrontendApplication.getCurrentChattingGroupID());
        } else {
            FrontendApplication.getCurrentClient().sendMessage("disableButtons" );
            FrontendApplication.getCurrentClient().sendMessage("InviteToGroup#" + user.getUsername() + "#" + invitableUser.getAccountID());


            chatController.disableDiscussionGroupButton();
        }

        chatController.closeInviteBox();

    }


    public boolean isGroupMember() {
        return groupMember;
    }

    public void setGroupMember(boolean groupMember) {
        this.groupMember = groupMember;
    }
}

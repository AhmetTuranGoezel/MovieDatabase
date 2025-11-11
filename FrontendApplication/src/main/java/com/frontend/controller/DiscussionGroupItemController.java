package com.frontend.controller;

import com.frontend.FrontendApplication;
import com.frontend.model.DiscussionGroup;
import com.frontend.model.Requests;
import com.frontend.model.User;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.eclipse.jetty.client.api.Request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class DiscussionGroupItemController extends SceneController {

    @FXML
    private Hyperlink groupName;

    @FXML
    private Button buttonJoinGroup;

    @FXML
    private ImageView img;

    private Requests request = new Requests();

    private DiscussionGroup group = new DiscussionGroup();


    public void setDiscussionGroups(DiscussionGroup group) {

        this.group = group;

        groupName.setText(group.getGroupName());

        if (group.getGroupImg() != null) {
            byte[] bytes = Base64.getDecoder().decode(group.getGroupImg());
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            Image image = new Image(bais);
            img.setImage(image);
        }

        for (User member : group.getGroupMember()) {
            if (member.getAccountID() == FrontendApplication.getCurrentAccount().getAccountID()) {
                buttonJoinGroup.setDisable(true);
            }
        }

    }

    public void goToGroupProfile() throws IOException, InterruptedException {

        FrontendApplication.setSelectedGroup(group);
        switchToDiscussionGroupProfile();
    }

    public void join() throws IOException, InterruptedException {
        request.post("/discussiongroup/" + group.getGroupId() + "/" + FrontendApplication.getCurrentAccount().getAccountID() + "/", "");
        FrontendApplication.setSelectedGroup(group);
        switchToDiscussionGroupProfile();
    }
}
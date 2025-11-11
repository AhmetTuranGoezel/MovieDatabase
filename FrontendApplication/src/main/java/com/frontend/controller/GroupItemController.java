package com.frontend.controller;

import com.frontend.FrontendApplication;
import com.frontend.model.DiscussionGroup;
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

public class GroupItemController extends SceneController {

    @FXML
    private ImageView gImg;

    @FXML
    private Hyperlink gName;

    private DiscussionGroup group = new DiscussionGroup();


    public void setGroup(DiscussionGroup group) {

        this.group = group;
        gName.setText(group.getGroupName());

        if (group.getGroupImg() != null) {
            byte[] bytes = Base64.getDecoder().decode(group.getGroupImg());
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            Image image = new Image(bais);
            gImg.setImage(image);

        }
    }

    public void goToGroupProfile() throws IOException, InterruptedException {

        FrontendApplication.setSelectedGroup(group);
        switchToDiscussionGroupProfile();
    }
}

package com.frontend.controller;

import com.frontend.FrontendApplication;
import com.frontend.model.DiscussionGroup;
import com.frontend.model.Requests;
import com.frontend.model.User;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class DiscussionGroupProfileController extends SceneController implements Initializable {

    @FXML
    private Label groupName;
    @FXML
    private ImageView groupImg;
    @FXML
    private Button joinButton;
    @FXML
    private MenuButton memberOption;

    @FXML
    private Button chat;
    @FXML
    private VBox participantsOverview;
    private Requests request = new Requests();
    private Gson gson = new Gson();
    private DiscussionGroup group;


    public void joinGroup() throws IOException, InterruptedException {
        request.post("/discussiongroup/" + group.getGroupId() + "/" + FrontendApplication.getCurrentAccount().getAccountID() + "/", "");
        switchToDiscussionGroupProfile();
    }

    public void leaveGroup() throws IOException, InterruptedException {

        request.delete("/discussiongroups/deleteuser/" + FrontendApplication.getSelectedGroup().getGroupId() + "/" + FrontendApplication.getCurrentAccount().getAccountID());
        switchToDiscussionGroup();
    }

    public void startChat() throws IOException, InterruptedException {

        FrontendApplication.setCurrentChattingGroupID(group.getGroupId());
        User me = (User) FrontendApplication.getCurrentAccount();
        StringBuilder stringBuilder = new StringBuilder();
        for (User user : group.getGroupMember()) {
            if (user.getAccountID() != me.getAccountID()) {
                stringBuilder.append(user.getAccountID());
                stringBuilder.append("*");
            }
        }
        stringBuilder.setLength(Math.max(stringBuilder.length() - 1, 0));
        FrontendApplication.getCurrentClient().sendMessage("InviteGroup#" + me.getUsername() + "#" + stringBuilder.toString()+"#"+FrontendApplication.getCurrentChattingGroupID());
        System.out.println("InviteGroup#" + me.getUsername() + "#" + stringBuilder.toString()+"#"+FrontendApplication.getCurrentChattingGroupID());
        switchToChat();
    }

    public void openEditGroupWindow() throws IOException, InterruptedException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/editDiscussionGroup.fxml"));
        FrontendApplication.setCurrentFxmlLoader(loader);
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));

        stage.centerOnScreen();
        stage.setResizable(false);
        stage.alwaysOnTopProperty();
        stage.setOnCloseRequest(e -> {
            try {
                switchToStart();
            } catch (IOException | InterruptedException exc) {
                exc.printStackTrace();
            }
        });

        stage.show();
        FrontendApplication.setCurrentPopup(stage);
        FrontendApplication.updateCurrentAccount();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        System.out.println(FrontendApplication.getCurrentAccount().getAccountID());

        try {
            request.get("/discussiongroups/" + FrontendApplication.getSelectedGroup().getGroupId());
            group = gson.fromJson(request.getMessage(), DiscussionGroup.class);

            groupName.setText(group.getGroupName());
            if (group.getGroupImg() != null) {
                byte[] bytes = Base64.getDecoder().decode(group.getGroupImg());
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                Image image = new Image(bais);
                groupImg.setImage(image);
            }

            for (User member : group.getGroupMember()) {
                if (member.getAccountID() == FrontendApplication.getCurrentAccount().getAccountID()) {
                    joinButton.setVisible(false);
                    chat.setVisible(true);
                    memberOption.setVisible(true);
                    break;
                }else{
                    joinButton.setVisible(true);
                    chat.setVisible(false);
                    memberOption.setVisible(false);

                }
            }

            for (User participants : this.group.getGroupMember()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/views/discussionGroupMember.fxml"));
                HBox hBox = fxmlLoader.load();
                DiscussionGroupMemberItemController discussionGroupViewItemController = fxmlLoader.getController();
                discussionGroupViewItemController.setParticipant(participants);
                participantsOverview.getChildren().add(hBox);
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}

package com.frontend.controller;

import com.frontend.FrontendApplication;
import com.frontend.model.DiscussionGroup;
import com.frontend.model.Requests;
import com.frontend.model.User;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class DiscussionGroupController extends SceneController implements Initializable {

    @FXML
    private Label createErrorMessage;

    @FXML
    private ImageView groupimg;

    @FXML
    private TextField groupname;

    @FXML
    private VBox groupsOverview;

    @FXML
    private TextField tfsearch;

    private int visibility;

    private String groupImage;

    @FXML
    private MenuButton visibilityCreate;

    private Requests request = new Requests();

    private Gson gson = new Gson();

    private User me = new User();
    private List<DiscussionGroup> groupList = new LinkedList<>();


    public void add() throws IOException {

        FileChooser fileChooser = new FileChooser();
        File file = new File(fileChooser.showOpenDialog(null).getPath());
        byte[] bytes = FileUtils.readFileToByteArray(file);
        groupImage = Base64.getEncoder().encodeToString(bytes);


        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        Image image = new Image(bais);
        groupimg.setImage(image);
    }

    public void create() throws IOException, InterruptedException {

        createErrorMessage.setText(null);

        if (groupname.getText().isEmpty()) {
            createErrorMessage.setText("* Mandatory Field");

        } else {
            DiscussionGroup newDiscussionGroup = new DiscussionGroup();
            newDiscussionGroup.setGroupName(groupname.getText());
            newDiscussionGroup.setGroupImg(groupImage);
            newDiscussionGroup.setVisible(visibility);
            List<User> member = Arrays.asList((User) FrontendApplication.getCurrentAccount());
            newDiscussionGroup.setGroupMember(member);


            request.post("/discussiongroup/create/", gson.toJson(newDiscussionGroup));

            if (request.getStatus().is2xxSuccessful()) {
                switchToDiscussionGroup();
            }
            if (request.getStatus().is4xxClientError()) {
                createErrorMessage.setText(request.getMessage());
            }
        }
    }

    public void everyone() {
        visibility = 0;
        visibilityCreate.setText("Everyone");
    }

    public void friends() {
        visibility = 1;
        visibilityCreate.setText("Friends");
    }

    public void searchGroup() throws IOException, InterruptedException {

        if (!tfsearch.getText().isEmpty()) {

            groupsOverview.getChildren().clear();

            for (DiscussionGroup group : groupList) {

                if (group.getGroupName().toLowerCase().contains(tfsearch.getText().toLowerCase())) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/views/discussionGroupItem.fxml"));
                    HBox hBox = fxmlLoader.load();
                    DiscussionGroupItemController discussionGroupItemController = fxmlLoader.getController();
                    discussionGroupItemController.setDiscussionGroups(group);

                    if (group.getVisible() == 1) {

                        request.get("/visibilityCheck/" + group.getGroupId() + "/" + me.getAccountID());
                        if (request.getMessage().equals("true")) {
                            groupsOverview.getChildren().add(hBox);
                        }
                    }
                    else {
                        groupsOverview.getChildren().add(hBox);
                    }
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        me = (User) FrontendApplication.getCurrentAccount();

        groupsOverview.getChildren().clear();

        try {

            byte[] byteArray = IOUtils.toByteArray(getClass().getResourceAsStream("/discussionG.png"));
            String groupImg = Base64.getEncoder().encodeToString(byteArray);
            groupImage=groupImg;

            request.get("/discussiongroups/");
            String groupAsJson = request.getMessage();
            DiscussionGroup[] groupArray = gson.fromJson(groupAsJson, DiscussionGroup[].class);
            groupList = List.of(groupArray);


            for (DiscussionGroup group : groupList) {

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/views/discussionGroupItem.fxml"));
                HBox hBox = fxmlLoader.load();
                DiscussionGroupItemController discussionGroupItemController = fxmlLoader.getController();
                discussionGroupItemController.setDiscussionGroups(group);

                if (group.getVisible() == 1) {

                    request.get("/visibilityCheck/" + group.getGroupId() + "/" + me.getAccountID());
                    if (request.getMessage().equals("true")) {
                        groupsOverview.getChildren().add(hBox);
                    }
                }
                else {
                    groupsOverview.getChildren().add(hBox);
                }
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}

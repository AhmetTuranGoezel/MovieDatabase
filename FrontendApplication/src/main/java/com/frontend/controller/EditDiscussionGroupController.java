package com.frontend.controller;

import com.frontend.FrontendApplication;
import com.frontend.model.DiscussionGroup;
import com.frontend.model.Requests;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class EditDiscussionGroupController extends SceneController implements Initializable {

    @FXML
    private ImageView imageview;
    @FXML
    private Label errorMessage;
    @FXML
    private Label mandatoryField;
    @FXML
    private TextField groupnametf;
    @FXML
    private MenuButton visibilityCreate;

    private int visibility;

    private String groupImage;

    private DiscussionGroup group;

    private Requests request = new Requests();
    private Gson gson = new Gson();

    public void addGroupImage() throws IOException {

        FileChooser fileChooser = new FileChooser();
        File file = new File(fileChooser.showOpenDialog(null).getPath());
        byte[] bytes = FileUtils.readFileToByteArray(file);
        groupImage = Base64.getEncoder().encodeToString(bytes);


        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        Image image = new Image(bais);
        imageview.setImage(image);

    }

    public void deleteGroupImage() throws IOException {

        byte[] byteArray = IOUtils.toByteArray(getClass().getResourceAsStream("/discussionG.png"));
        groupImage = Base64.getEncoder().encodeToString(byteArray);

        ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
        Image image = new Image(bais);
        imageview.setImage(image);
    }

    public void editGroup() throws IOException, InterruptedException {

        errorMessage.setText(null);

        group.setGroupName(groupnametf.getText());
        group.setGroupImg(groupImage);
        group.setVisible(visibility);

        if (!groupnametf.getText().isEmpty()) {

            request.put("/discussiongroup/update/", gson.toJson(group));

            if (request.getStatus().is2xxSuccessful()) {
                FrontendApplication.getCurrentPopup().close();
                switchToDiscussionGroupProfile();
            }
            if (request.getStatus().is4xxClientError()) {
                errorMessage.setText(request.getMessage());
            }
        }else{
            mandatoryField.setText("*");
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        System.out.println(FrontendApplication.getSelectedGroup().getGroupId());

        try {
            request.get("/discussiongroups/" + FrontendApplication.getSelectedGroup().getGroupId());
            group = gson.fromJson(request.getMessage(), DiscussionGroup.class);

            visibility = group.getVisible();
            groupnametf.setText(group.getGroupName());
            groupImage = group.getGroupImg();

            if(group.getVisible() == 0) {
                visibilityCreate.setText("Everyone");
            }else if(group.getVisible() == 1){
                visibilityCreate.setText("Friends");
            }

            if (group.getGroupImg() != null) {
                byte[] bytes = Base64.getDecoder().decode(group.getGroupImg());
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                Image image = new Image(bais);
                imageview.setImage(image);
            }

            switch (group.getVisible()) {
                case 1:
                    visibilityCreate.setText("Everyone");
                    visibility = 0;
                    break;

                case 2:
                    visibilityCreate.setText("Friends");
                    visibility = 1;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

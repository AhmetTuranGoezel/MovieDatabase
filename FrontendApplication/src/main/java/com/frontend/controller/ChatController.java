package com.frontend.controller;

import com.frontend.FrontendApplication;
import com.frontend.model.Account;
import com.frontend.model.Client;
import com.frontend.model.Requests;
import com.frontend.model.User;
import com.google.gson.Gson;
import com.sun.javafx.fxml.builder.JavaFXSceneBuilder;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;


public class ChatController extends SceneController implements Initializable {

    @FXML
    private TextField chatTF;
    @FXML
    private VBox chatVbox;
    @FXML
    private ScrollPane chatSP;
    @FXML
    private VBox invitePopUp;
    @FXML
    private ScrollPane inviteSP;
    @FXML
    private Button inviteFromGroupButton;

    private Client client;

    private boolean toggle = false;
    private Requests request = new Requests();
    private Gson gson = new Gson();


    public void endChat() throws IOException, InterruptedException {
        FrontendApplication.getCurrentClient().sendMessage("CloseChat#");
        FrontendApplication.setCurrentChattingGroupID(null);
        switchToSocial();
    }


    public void sendMessage() throws IOException {

        if (!chatTF.getText().isEmpty()) {

            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5));
            Text text = new Text(chatTF.getText());
            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle("-fx-background-color: rgb(186, 218, 199);" + "-fx-background-radius: 25px;");
            textFlow.setPadding(new Insets(10));
            text.setFill(Color.color(0.176, 0.18, 0.176));
            hBox.getChildren().add(textFlow);
            chatVbox.getChildren().add(hBox);
            System.out.println("Nutzer Chatbubble erstellt");


            System.out.println("Start sending message");
            FrontendApplication.getCurrentClient().sendMessage("Chat#" + chatTF.getText());
            System.out.println(" send message to clienthandler ok ");

            chatTF.clear();
        }

    }


    public void inviteFriend() throws IOException, InterruptedException {
        if (!toggle) {
            inviteSP.setVisible(true);
            invitePopUp.setVisible(true);

            request.get("/users/friends/" + FrontendApplication.getCurrentAccount().getAccountID());
            String usersAsJson = request.getMessage();
            User[] userArray = gson.fromJson(usersAsJson, User[].class);
            for (User friend : userArray) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/views/chatInviteItem.fxml"));
                HBox hBox = fxmlLoader.load();
                ChatInvItemController chatInvItemController = fxmlLoader.getController();
                chatInvItemController.setGroupMember(false);
                chatInvItemController.updateData(friend);
                invitePopUp.getChildren().add(hBox);

            }
            toggle = true;

        } else {
            closeInviteBox();


        }
    }

    public void inviteGroup() throws IOException, InterruptedException {
        if (!toggle) {
            inviteSP.setVisible(true);
            invitePopUp.setVisible(true);

            request.get("/discussiongroup/member/" + FrontendApplication.getCurrentChattingGroupID());
            String usersAsJson = request.getMessage();
            User[] userArray = gson.fromJson(usersAsJson, User[].class);
            for (User groupMember : userArray) {
                if(!(groupMember.getAccountID() == FrontendApplication.getCurrentAccount().getAccountID())) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/views/chatInviteItem.fxml"));
                    HBox hBox = fxmlLoader.load();
                    ChatInvItemController chatInvItemController = fxmlLoader.getController();
                    chatInvItemController.updateData(groupMember);
                    invitePopUp.getChildren().add(hBox);
                    chatInvItemController.setGroupMember(true);
                    toggle = true;
                }
            }
        } else {
            closeInviteBox();


        }
    }


    public void closeInviteBox() {
        invitePopUp.getChildren().clear();
        inviteSP.setVisible(false);
        invitePopUp.setVisible(false);
        toggle=false;
    }

    public void disableDiscussionGroupButton(){
        inviteFromGroupButton.setDisable(true);
    }


    public void buildReceivedMessage(String messageFromClientHandler) {


        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5));
        Text text = new Text(messageFromClientHandler);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: rgb(45, 46, 45);" + "-fx-background-radius: 25px;");
        textFlow.setPadding(new Insets(10));
        text.setFill(Color.color(0.729, 0.854, 0.780));
        hBox.getChildren().add(textFlow);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                chatVbox.getChildren().add(hBox);
            }
        });


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if(FrontendApplication.getCurrentChattingGroupID()==null){
            disableDiscussionGroupButton();
        }
        chatVbox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                chatSP.setVvalue((Double) t1);

            }
        });

    }
}

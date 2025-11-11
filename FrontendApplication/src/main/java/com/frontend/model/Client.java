package com.frontend.model;

import com.frontend.FrontendApplication;
import com.frontend.controller.ChatController;
import com.frontend.controller.FriendslistItemController;
import com.frontend.controller.SceneController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;
import java.util.Optional;
import java.util.StringTokenizer;

public class Client {

    Socket socket;
    String name = "";
    PrintWriter writer;
    BufferedReader bfreader;
    StringTokenizer tokenizer;

    public Client(Socket socket) throws IOException {

        this.socket = socket;
        this.bfreader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.name = name;
        sendInfo();
    }


    public void sendMessage(String messageToSend) throws IOException {

        String message = messageToSend;
        writer.println(message);
    }

    public void sendInfo() throws IOException {

        User user = (User) FrontendApplication.getCurrentAccount();
        writer.println(user.getAccountID() + "#" + user.getUsername());
    }


    public void listenMessage() throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (socket.isConnected()) {
                    try {
                        String receivedMessage = bfreader.readLine();
                        System.out.println(receivedMessage);
                        tokenizer = new StringTokenizer(receivedMessage, "#");
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                ChatController chatController;
                                switch (tokenizer.nextToken()) {

                                    //Method#CurrentChattingGroup#SenderUsername#SenderID#ChatgroupID#(CurrentChattingGroupID)
                                    case "Alert":
                                        System.out.println("bin im alert");
                                        Alert alert =
                                                new Alert(Alert.AlertType.INFORMATION,
                                                        tokenizer.nextToken() + " wants to Chat with you. Do you want to start a Live Chat?",
                                                        ButtonType.YES, ButtonType.NO);
                                        alert.setTitle("LiveChat request");
                                        int clientID = Integer.parseInt(tokenizer.nextToken());
                                        Optional<ButtonType> result = alert.showAndWait();
                                        if (!result.isPresent() || result.get() == ButtonType.NO) {
                                            writer.println("Decline#" + clientID);

                                        } else if (result.get() == ButtonType.YES) {
                                            writer.println("StartChat#" + tokenizer.nextToken() + "#" + clientID);
                                            if (tokenizer.hasMoreTokens()) {
                                                FrontendApplication.setCurrentChattingGroupID(Integer.parseInt(tokenizer.nextToken()));
                                            }
                                            Platform.runLater(new Runnable() {
                                                @Override
                                                public void run() {
                                                    SceneController sceneController = new SceneController();
                                                    try {
                                                        sceneController.switchToChat();
                                                    } catch (IOException | InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                        }
                                        break;

                                    case "Chat":
                                        chatController = FrontendApplication.getCurrentFxmlLoader().getController();
                                        String message = tokenizer.nextToken();
                                        System.out.println(message);
                                        chatController.buildReceivedMessage(message);
                                        break;


                                    case "disableButtons":
                                        chatController = FrontendApplication.getCurrentFxmlLoader().getController();
                                        chatController.disableDiscussionGroupButton();
                                        break;

                                }
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }


    public void closeSocket() throws IOException {
        bfreader.close();
        writer.close();
        socket.close();

    }


}






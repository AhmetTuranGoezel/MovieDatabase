package com.frontend;

import com.frontend.model.*;
import com.google.gson.Gson;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;


public class FrontendApplication extends Application {

    private static Account currentAccount;
    private static Client currentClient;
    private static Stage currentStage;
    private static Stage currentPopup;
    private static FXMLLoader currentFxmlLoader;
    private static Movie selectedMovie;
    private static User selectedUser;
    private static Report selectedReport;
    private static DiscussionGroup selectedGroup;
    private static WatchpartyInvitation selectedWpInv;

    private static Integer currentChattingGroupID;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage mainStage) throws IOException {
        currentFxmlLoader = new FXMLLoader();
        currentFxmlLoader.setLocation(getClass().getResource("/views/login.fxml"));
        //currentFxmlLoader.setLocation(getClass().getResource("/views/userStatistic.fxml"));
        Parent root = currentFxmlLoader.load();

        currentStage = mainStage;
        currentStage.setTitle("SEP Filmdatenbank");
        currentStage.setScene(new Scene(root));
        currentStage.show();
        currentStage.setResizable(false);
        currentStage.getIcons().add(new Image(getClass().getResourceAsStream("/NoBGLogo03.png")));
        currentStage.setOnCloseRequest(e -> {
            try {
                logout();
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        });

    }

    public static void logout() throws IOException, InterruptedException {
        if (currentClient != null) {
            currentClient.sendMessage("LogOut#");
            //currentClient.closeSocket();
        }
        if (currentAccount != null) {

            if (!currentAccount.isAdmin()) {
                Requests request = new Requests();
                Gson gson = new Gson();

                request.get("/users/" + FrontendApplication.getCurrentAccount().getAccountID());
                User user = gson.fromJson(request.getMessage(), User.class);
                request.put("/users/update/"+user.getAccountID()+"/false", "");
            }
            setCurrentAccount(null);
        }
    }

    public static Account getCurrentAccount() {
        return currentAccount;
    }

    public static void setCurrentAccount(Account currentAccount) {
        FrontendApplication.currentAccount = currentAccount;
    }

    public static void updateCurrentAccount() throws IOException, InterruptedException {

        if (currentAccount != null) {
            Requests request = new Requests();
            Gson gson = new Gson();

            if (currentAccount.isAdmin()) {
                request.get("/systemadministrators/" + currentAccount.getAccountID());
                Systemadministrator admin = gson.fromJson(request.getMessage(), Systemadministrator.class);
                setCurrentAccount(admin);
            } else {
                request.get("/users/" + currentAccount.getAccountID());
                User user = gson.fromJson(request.getMessage(), User.class);
                setCurrentAccount(user);
            }
        }
    }

    public static Stage getCurrentStage() {
        return currentStage;
    }

    public static void setCurrentStage(Stage currentStage) {
        FrontendApplication.currentStage = currentStage;
    }

    public static Report getSelectedReport() {
        return selectedReport;
    }

    public static void setSelectedReport(Report selectedReport) {
        FrontendApplication.selectedReport = selectedReport;
    }

    public static Movie getSelectedMovie() {
        return selectedMovie;
    }

    public static void setSelectedMovie(Movie selectedMovie) {
        FrontendApplication.selectedMovie = selectedMovie;
    }

    public static Stage getCurrentPopup() {
        return currentPopup;
    }

    public static void setCurrentPopup(Stage currentPopup) {
        FrontendApplication.currentPopup = currentPopup;
    }

    public static User getSelectedUser() {
        return selectedUser;
    }

    public static void setSelectedUser(User selectedUser) {
        FrontendApplication.selectedUser = selectedUser;
    }

    public static Client getCurrentClient() {
        return currentClient;
    }

    public static void setCurrentClient(Client currentClient) {
        FrontendApplication.currentClient = currentClient;
    }

    public static FXMLLoader getCurrentFxmlLoader() {
        return currentFxmlLoader;
    }

    public static void setCurrentFxmlLoader(FXMLLoader currentFxmlLoader) {
        FrontendApplication.currentFxmlLoader = currentFxmlLoader;
        FrontendApplication.currentFxmlLoader.setController(currentFxmlLoader.getController());
    }

    public static DiscussionGroup getSelectedGroup() {
        return selectedGroup;
    }

    public static void setSelectedGroup(DiscussionGroup selectedGroup) {
        FrontendApplication.selectedGroup = selectedGroup;
    }

    public static WatchpartyInvitation getSelectedWpInv() {
        return selectedWpInv;
    }

    public static void setSelectedWpInv(WatchpartyInvitation selectedWpInv) {
        FrontendApplication.selectedWpInv = selectedWpInv;
    }


    public static Integer getCurrentChattingGroupID() {
        return currentChattingGroupID;
    }

    public static void setCurrentChattingGroupID(Integer currentChattingGroupID) {
        FrontendApplication.currentChattingGroupID = currentChattingGroupID;
    }
}

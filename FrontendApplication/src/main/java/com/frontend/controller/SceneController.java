package com.frontend.controller;

import com.frontend.FrontendApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;


public class SceneController {


    public void switchToSignUpUser() throws IOException, InterruptedException {
        SceneSwitch("/views/signupUser.fxml");

    }

    public void switchToSignUpAdmin() throws IOException, InterruptedException {
        SceneSwitch("/views/signupAdministrator.fxml");

    }

    public void switchToAuth() throws IOException, InterruptedException {
        SceneSwitch("/views/authentication.fxml");
    }

    public void switchToMovielist() throws IOException, InterruptedException {


        if (FrontendApplication.getCurrentAccount().isAdmin()) {
            SceneSwitch("/views/movielistAdmin.fxml");
        } else {
            SceneSwitch("/views/movielistUser.fxml");
        }
    }

    public void switchToLogin() throws IOException, InterruptedException {
        SceneSwitch("/views/login.fxml");

    }

    public void switchToForeignUserProfile() throws IOException, InterruptedException {
        if (FrontendApplication.getCurrentAccount().isAdmin()) {
            SceneSwitch("/views/foreignUserProfileAdminView.fxml");
        } else {
            SceneSwitch("/views/foreignUserProfile.fxml");
        }
    }

    public void switchToOwnProfile() throws IOException, InterruptedException {
        SceneSwitch("/views/profileUser.fxml");
    }

    public void switchToSettings() throws IOException, InterruptedException {
        SceneSwitch("/views/settings.fxml");
    }


    public void switchToSocial() throws IOException, InterruptedException {
        SceneSwitch("/views/friendslist.fxml");
    }

    public void switchToChat() throws IOException, InterruptedException {
        SceneSwitch("/views/chat.fxml");
    }

    public void switchToWatchList() throws IOException, InterruptedException {
        SceneSwitch("/views/watchlist.fxml");
    }

    public void switchToHistory() throws IOException, InterruptedException {
        SceneSwitch("/views/history.fxml");
    }

    public void SwitchToAddmovie() throws IOException, InterruptedException {
        SceneSwitch("/views/addMovie.fxml");
    }

    public void SwitchToUpdatemovie() throws IOException, InterruptedException {
        SceneSwitch("/views/updateMovie.fxml");
    }

    public void logOut() throws IOException, InterruptedException {
        FrontendApplication.logout();
        switchToLogin();
    }

    public void SwitchToReport() throws IOException, InterruptedException {
        SceneSwitch("/views/reportAdmin.fxml");
    }

    public void switchToStart() throws IOException, InterruptedException {
        if(!FrontendApplication.getCurrentAccount().isAdmin()) {
            SceneSwitch("/views/startView.fxml");
        }
    }

    public void switchToStatistics() throws IOException, InterruptedException {
        if (FrontendApplication.getCurrentAccount().isAdmin()) {
            SceneSwitch("/views/adminStatistic.fxml");
        } else {
            SceneSwitch("/views/userStatistic.fxml");
        }
    }

    public void switchToDiscussionGroup() throws IOException, InterruptedException {
        SceneSwitch("/views/discussionGroup.fxml");
    }

    public void switchToDiscussionGroupProfile() throws IOException, InterruptedException {
        SceneSwitch("/views/discussionGroupProfile.fxml");
    }

    public void switchToEditDiscussionGroup() throws IOException, InterruptedException {
        SceneSwitch("/views/editDiscussionGroup.fxml");
    }

    public void switchToWatchPartyInvList() throws IOException, InterruptedException {
        SceneSwitch("/views/watchparty.fxml");
    }

    public void SceneSwitch(String pathFXML) throws IOException, InterruptedException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(pathFXML));
        Scene newScene = new Scene(fxmlLoader.load());
        FrontendApplication.setCurrentFxmlLoader(fxmlLoader);
        FrontendApplication.getCurrentStage().setScene(newScene);

        FrontendApplication.updateCurrentAccount();
    }


}

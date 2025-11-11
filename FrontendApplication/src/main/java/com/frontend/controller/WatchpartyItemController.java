package com.frontend.controller;

import com.frontend.FrontendApplication;
import com.frontend.model.Requests;
import com.frontend.model.User;
import com.frontend.model.Watchparty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WatchpartyItemController extends SceneController {
    @FXML
    private Hyperlink userName;
    @FXML
    private Label date;

    @FXML
    private Label time;

    @FXML
    private Label title;

    private User wpPartner = new User();

    public void setWatchparty(Watchparty watchparty) {


        if(watchparty.getHost().getAccountID()==FrontendApplication.getCurrentAccount().getAccountID()){
            wpPartner = watchparty.getGuest();
            userName.setText(watchparty.getGuest().getUsername());
        }else{
            wpPartner = watchparty.getHost();
            userName.setText(watchparty.getHost().getUsername());
        }
        title.setText(watchparty.getMovie().getTitle());
        date.setText(watchparty.getDate());
        time.setText(watchparty.getTime());

    }

    public void goToProfile() throws IOException, InterruptedException {

        FrontendApplication.setSelectedUser(wpPartner);
        switchToForeignUserProfile();
    }

}

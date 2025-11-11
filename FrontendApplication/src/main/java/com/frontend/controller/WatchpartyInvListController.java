package com.frontend.controller;


import com.frontend.FrontendApplication;
import com.frontend.model.*;
import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class WatchpartyInvListController extends SceneController implements Initializable {

    @FXML
    private Label labelUsername;
    @FXML
    private Button accept;

    @FXML
    private TableColumn<WatchpartyInvitation, String> columnDate;

    @FXML
    private TableColumn<WatchpartyInvitation, String> columnMovie;

    @FXML
    private TableColumn<WatchpartyInvitation, String> columnTime;

    @FXML
    private TableColumn<WatchpartyInvitation, String> columnUsername;

    @FXML
    private Button reject;

    @FXML
    private VBox vboxMessage;

    @FXML
    private Text watchpartyMessage;

    @FXML
    private TableView<WatchpartyInvitation> tableWatchparty;

    private Requests request = new Requests();
    private Gson gson = new Gson();
    private WatchpartyInvitation watchpartyInvitation = new WatchpartyInvitation();
    private Watchparty watchparty = new Watchparty();
    private Notification notification = new Notification();


    public void acceptWatchpartyInvitation() throws IOException, InterruptedException {
    request.get("/watchpartyinvitation/"+FrontendApplication.getSelectedWpInv().getWatchpartyInvitationId());
    watchpartyInvitation=gson.fromJson(request.getMessage(),WatchpartyInvitation.class);

    watchparty.setDate(watchpartyInvitation.getDate());
    watchparty.setTime(watchpartyInvitation.getTime());
    watchparty.setHost(watchpartyInvitation.getSender());
    watchparty.setGuest(watchpartyInvitation.getRecipient());
    watchparty.setMovie(watchpartyInvitation.getMovie());


    request.post("/watchparty/create/",gson.toJson(watchparty));

        notification.setType(Notification.Type.WatchpartyAccepted);
        notification.setText(watchpartyInvitation.getRecipient(), Notification.Type.WatchpartyAccepted,watchpartyInvitation.getMovie());
        notification.setNotificatedUser(watchpartyInvitation.getSender());
        notification.setNotificationImg(Notification.Type.WatchpartyAccepted);
        notification.setMovie(watchpartyInvitation.getMovie());


        request.post("/notification/create/",gson.toJson(notification));

    request.delete("/watchpartyinvitation/delete/"+ FrontendApplication.getSelectedWpInv().getWatchpartyInvitationId());
    switchToSocial();
    }

    public void rejectWatchpartyInvitation() throws IOException, InterruptedException {

        request.delete("/watchpartyinvitation/delete/"+ FrontendApplication.getSelectedWpInv().getWatchpartyInvitationId());
        switchToWatchPartyInvList();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            request.get("/watchpartyinvitations/" + FrontendApplication.getCurrentAccount().getAccountID());
            WatchpartyInvitation[] watchpartyInvitations = gson.fromJson(request.getMessage(), WatchpartyInvitation[].class);

            ObservableList<WatchpartyInvitation> watchpartyInvList = FXCollections.observableArrayList(watchpartyInvitations);

            for(WatchpartyInvitation inv : watchpartyInvList){
                inv.setUsernameSender(inv.getSender().getUsername());
                inv.setMovieTitle(inv.getMovie().getTitle());
            }

            columnUsername.setCellValueFactory(new PropertyValueFactory<WatchpartyInvitation, String>("usernameSender"));
            columnMovie.setCellValueFactory(new PropertyValueFactory<WatchpartyInvitation, String>("movieTitle"));
            columnDate.setCellValueFactory(new PropertyValueFactory<WatchpartyInvitation, String>("date"));
            columnTime.setCellValueFactory(new PropertyValueFactory<WatchpartyInvitation, String>("time"));

            tableWatchparty.setItems(watchpartyInvList);

        } catch (Exception e) {
            e.printStackTrace();
        }

        tableWatchparty.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (tableWatchparty.getSelectionModel().getSelectedItem() != null) {
                    WatchpartyInvitation wpInv = tableWatchparty.getSelectionModel().getSelectedItem();
                    FrontendApplication.setSelectedWpInv(wpInv);

                    labelUsername.setText(wpInv.getSender().getUsername());
                    watchpartyMessage.setText(wpInv.getText());
                    vboxMessage.setVisible(true);
                    accept.setVisible(true);
                    reject.setVisible(true);
                }
            }
        });


    }
}

package com.frontend.controller;

import com.frontend.FrontendApplication;
import com.frontend.model.*;
import com.google.gson.Gson;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;


import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;




public class WatchPartyInvController extends SceneController implements Initializable {


    @FXML
    private TabPane communityTabPane;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ListView<String> listViewFriends;

    @FXML
    private TextArea optText;

    @FXML
    private Label mandatorySelectUser;

    @FXML
    private Spinner<Integer> timeSpinner1;

    @FXML
    private Spinner<Integer> timeSpinner2;

    @FXML
    private ComboBox amPm;


    private Requests request = new Requests();
    private Gson gson = new Gson();
    private WatchpartyInvitation watchpartyInvitation = new WatchpartyInvitation();
    private Notification notification = new Notification();


    public void sendInvitation() throws IOException, InterruptedException {

        if (FrontendApplication.getSelectedUser() == null) {
            mandatorySelectUser.setText("*");
        } else {
            String hours = timeSpinner1.getValue().toString();
            String minutes = timeSpinner2.getValue().toString();
            watchpartyInvitation.setMovie(FrontendApplication.getSelectedMovie());
            watchpartyInvitation.setSender((User) FrontendApplication.getCurrentAccount());
            watchpartyInvitation.setRecipient(FrontendApplication.getSelectedUser());
            watchpartyInvitation.setDate(datePicker.getValue().toString());


            if (timeSpinner1.getValue() >= 0 && timeSpinner1.getValue() <= 9) {
                hours = timeSpinner1.getValue().toString();
            }
            if (timeSpinner2.getValue() >= 0 && timeSpinner2.getValue() <= 9) {
                minutes = "0" + timeSpinner2.getValue().toString();
            }


            watchpartyInvitation.setTime(hours + ":" + minutes + " " + amPm.getValue());
            watchpartyInvitation.setText(optText.getText());
            request.post("/watchpartyinvitation/create/" + FrontendApplication.getSelectedUser().getAccountID(), gson.toJson(watchpartyInvitation));

            notification.setType(Notification.Type.WatchpartyInv);
            notification.setText(watchpartyInvitation.getSender(), Notification.Type.WatchpartyInv, watchpartyInvitation.getMovie());
            notification.setNotificatedUser(watchpartyInvitation.getRecipient());
            notification.setNotificationImg(Notification.Type.WatchpartyInv);
            notification.setMovie(watchpartyInvitation.getMovie());

            request.post("/notification/create/", gson.toJson(notification));
            FrontendApplication.getCurrentPopup().close();
            switchToStart();

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        FrontendApplication.setSelectedUser(null);

        datePicker.setValue(LocalDate.now());
        amPm.getItems().add("AM");
        amPm.getItems().add("PM");
        amPm.setValue("PM");
        System.out.println(amPm.getValue());

        listViewFriends.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    if(listViewFriends.getSelectionModel().getSelectedItem() != null) {
                        request.get("/user/" + listViewFriends.getSelectionModel().getSelectedItem());
                        FrontendApplication.setSelectedUser(gson.fromJson(request.getMessage(), User.class));
                        System.out.println("ID: " + FrontendApplication.getSelectedUser().getAccountID());
                    }

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

            try {

            request.get("/users/friends/" + FrontendApplication.getCurrentAccount().getAccountID());
            User[] friends = gson.fromJson(request.getMessage(), User[].class);

            //ObservableList<User> groupmembersOL = FXCollections.observableArrayList(friends);
            //listViewFriends.setItems(groupmembersOL);

            for(User user : friends){
                listViewFriends.getItems().add(user.getUsername());
            }

           request.get("/user/discussiongroups/" + FrontendApplication.getCurrentAccount().getAccountID());

            DiscussionGroup[] groups = gson.fromJson(request.getMessage(), DiscussionGroup[].class);

            for(DiscussionGroup group : groups) {
                Tab tab = new Tab();
                tab.setText(group.getGroupName());

                ListView list = new ListView();

                for(User user : group.getGroupMember()){
                    list.getItems().add(user.getUsername());
                }

                list.setOnMousePressed(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        try {
                            if(list.getSelectionModel().getSelectedItem() != null) {
                                request.get("/user/" + list.getSelectionModel().getSelectedItem());
                                FrontendApplication.setSelectedUser(gson.fromJson(request.getMessage(), User.class));
                            }

                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });

                //ObservableList<User> groupmembersOL = FXCollections.observableArrayList(group.getGroupMember());
                //list.setItems(groupmembersOL);

                tab.setContent(list);
                communityTabPane.getTabs().add(tab);

            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }



        SpinnerValueFactory<Integer> svf1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,12);
        svf1.setValue(0);
        timeSpinner1.setValueFactory(svf1);

        timeSpinner1.setEditable(true);
        SpinnerValueFactory<Integer> svf2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59);
        svf2.setValue(0);
        timeSpinner2.setValueFactory(svf2);
        timeSpinner2.setEditable(true);



    }
}

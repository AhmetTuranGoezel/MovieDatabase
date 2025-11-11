package com.frontend.controller;

import com.frontend.FrontendApplication;
import com.frontend.model.Notification;
import com.frontend.model.Requests;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;


public class NotificationItemController extends SceneController{

    @FXML
    private ImageView img;
    @FXML
    private Hyperlink notificationText;

    private Requests request = new Requests();
    private Notification notification = new Notification();



    public void setNotification(Notification notification){

        this.notification=notification;

        notificationText.setText(notification.getText());

        if (notification.getNotificationImg()!= null) {
            byte[] bytes = Base64.getDecoder().decode(notification.getNotificationImg());
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            Image image = new Image(bais);
            img.setImage(image);

        }

    }
    public void deleteNotification() throws IOException, InterruptedException {

        request.delete("/notification/delete/"+ notification.getNotificationId());


        if(FrontendApplication.getCurrentFxmlLoader().getLocation().equals(getClass().getResource("/views/startView.fxml"))) {
            StartController controller = FrontendApplication.getCurrentFxmlLoader().getController();
            controller.showOrHideNotifications();
            controller.showOrHideNotifications();
        }else{
            FriendslistController controller = FrontendApplication.getCurrentFxmlLoader().getController();
            controller.showOrHideNotifications();
            controller.showOrHideNotifications();
        }



    }

    public void clickOnNotification() throws IOException, InterruptedException {
        if(notification.getType().equals(Notification.Type.WatchpartyInv.toString())){
            switchToWatchPartyInvList();
        }else if(notification.getType().equals(Notification.Type.Report.toString())){
            switchToMovielist();
            MovielistController controller = FrontendApplication.getCurrentFxmlLoader().getController();
            controller.selectMovie(notification.getMovie());
        }else{
            switchToSocial();
        }
    }

}
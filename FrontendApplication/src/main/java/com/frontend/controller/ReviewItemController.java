package com.frontend.controller;

import com.frontend.FrontendApplication;
import com.frontend.model.Review;
import com.frontend.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class ReviewItemController extends SceneController {

    @FXML
    private ImageView img;
    @FXML
    private Hyperlink userName;
    @FXML
    private ImageView ratingImg;
    @FXML
    private Text ratingText;
    @FXML
    private Button buttonEditReview;
    private Review review;
    private User user;


    public void updateReview(Review review) {


        this.review = review;
        user = review.getUser();
        userName.setText(review.getUser().getUsername());
        ratingText.setText(review.getComment());


        if (user.getProfileImg() != null) {
            byte[] bytes = Base64.getDecoder().decode(user.getProfileImg());
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            Image image = new Image(bais);
            img.setImage(image);
        }


        for (int i = 1; i < 6; i++) {
            if (i == review.getRating()) {
                Image rating = new Image(getClass().getResourceAsStream("/" + i + ".png"));
                ratingImg.setImage(rating);
            }
        }

        if (review.getUser().getAccountID() != FrontendApplication.getCurrentAccount().getAccountID()) {
            buttonEditReview.setVisible(false);
        }


    }

    public void editReview() throws IOException, InterruptedException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/addReview.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        AddReviewController arc = loader.getController();
        arc.setDataForEditReview(review.getReviewId());

        stage.show();
        FrontendApplication.setCurrentPopup(stage);
    }

    public void goToProfile() throws IOException, InterruptedException {

        if (FrontendApplication.getCurrentAccount().getAccountID() == user.getAccountID()) {
            switchToOwnProfile();
        } else {
            FrontendApplication.setSelectedUser(user);
            switchToForeignUserProfile();
        }
    }

}



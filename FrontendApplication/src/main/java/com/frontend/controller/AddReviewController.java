package com.frontend.controller;

import com.frontend.FrontendApplication;
import com.frontend.model.Movie;
import com.frontend.model.Requests;
import com.frontend.model.Review;
import com.frontend.model.User;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddReviewController extends SceneController implements Initializable {

    @FXML
    private TextArea comment;

    @FXML
    public MenuButton addRating;

    @FXML
    public MenuItem oneS;

    @FXML
    public MenuItem twoS;

    @FXML
    public MenuItem threeS;

    @FXML
    public MenuItem fourS;

    @FXML
    public MenuItem fiveS;

    @FXML
    public Label rating;
    @FXML
    private Label errorMsg;

    private Review review = new Review();
    private Requests request = new Requests();
    private Gson gson = new Gson();

    public void saveReview() throws IOException, InterruptedException {


        if (review.getReviewId() == 0) {
            if (rating.getText() != "") {
                review.setRating(Integer.parseInt(rating.getText()));
                review.setComment(comment.getText());
                review.setUser((User) FrontendApplication.getCurrentAccount());
                //request.get("/movies/" + FrontendApplication.getSelectedId());
                //Movie movie = gson.fromJson(request.getMessage(), Movie.class);
                review.setMovie(FrontendApplication.getSelectedMovie());

                request.post("/reviews/create/"+ FrontendApplication.getCurrentAccount().getAccountID(), gson.toJson(review));
                if (request.getStatus().is4xxClientError()) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText(request.getMessage());
                    alert.setTitle("Could not post your Review");
                    alert.setResizable(false);
                    switchToStart();
                    alert.show();


                }

            } else {
                errorMsg.setText("Rating has to be set!");
                return;
            }
        } else {
            review.setRating(Integer.parseInt(rating.getText()));
            review.setComment(comment.getText());
            review.setUser((User) FrontendApplication.getCurrentAccount());
            review.setMovie(FrontendApplication.getSelectedMovie());
            request.put("/reviews/create/", gson.toJson(review));
        }
        FrontendApplication.getCurrentPopup().close();
        switchToStart();
    }

    public void setDataForEditReview(int reviewId) throws IOException, InterruptedException {


        request.get("/reviews/" + reviewId);
        review = gson.fromJson(request.getMessage(), Review.class);
        rating.setText(Integer.toString(review.getRating()));
        comment.setText(review.getComment());
        System.out.println(review.getReviewId());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            FrontendApplication.updateCurrentAccount();
        } catch (IOException | InterruptedException exc) {
            exc.printStackTrace();
        }

        //for (MenuItem menuItem : addRating.getItems()) {

            oneS.setOnAction(x -> {
                rating.setText("1");
            });


            twoS.setOnAction(x -> {
                rating.setText("2");
            });

            threeS.setOnAction(x -> {
                rating.setText("3");
            });

            fourS.setOnAction(x -> {
                rating.setText("4");
            });

            fiveS.setOnAction(x -> {
                rating.setText("5");
            });

       // }

    }


}
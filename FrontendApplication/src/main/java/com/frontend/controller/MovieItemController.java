package com.frontend.controller;

import com.frontend.FrontendApplication;
import com.frontend.model.Movie;
import com.frontend.model.Review;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class MovieItemController extends SceneController{

    @FXML
    private ImageView cover;

    @FXML
    private Label genre;

    @FXML
    private Hyperlink movietitle;

    @FXML
    private ImageView rImg;

    protected Movie movie = new Movie();


    public void setMovie(Movie movie)throws IOException, InterruptedException {

     this.movie=movie;
     genre.setText(movie.getCategory());
     movietitle.setText(movie.getTitle());


     double averageRating = movie.getAverageRating();

     if(averageRating >= 1 && averageRating <= 1.2){
         Image rating = new Image(getClass().getResourceAsStream("/1.png"));
         rImg.setImage(rating);
     }else if (averageRating >= 1.3 && averageRating <= 1.7) {
         Image rating = new Image(getClass().getResourceAsStream("/1.5.png"));
         rImg.setImage(rating);
     }else if(averageRating >= 1.8 && averageRating <= 2.2){
         Image rating = new Image(getClass().getResourceAsStream("/2.png"));
         rImg.setImage(rating);
     }else if(averageRating >= 2.3 && averageRating <= 2.7) {
         Image rating = new Image(getClass().getResourceAsStream("/2.5.png"));
         rImg.setImage(rating);
     }else if(averageRating >= 2.8 && averageRating <= 3.2) {
         Image rating = new Image(getClass().getResourceAsStream("/3.png"));
         rImg.setImage(rating);
     }else if(averageRating >= 3.3 && averageRating <= 3.7) {
         Image rating = new Image(getClass().getResourceAsStream("/3.5.png"));
         rImg.setImage(rating);
     }else if(averageRating >= 3.8 && averageRating <= 4.2) {
         Image rating = new Image(getClass().getResourceAsStream("/4.png"));
         rImg.setImage(rating);
     }else if(averageRating >= 4.3 && averageRating <= 4.7) {
         Image rating = new Image(getClass().getResourceAsStream("/4.5.png"));
         rImg.setImage(rating);
     }else if(averageRating >= 4.8 && averageRating <= 5) {
         Image rating = new Image(getClass().getResourceAsStream("/5.png"));
         rImg.setImage(rating);
     }

        if (movie.getCover() != null) {
            byte[] bytes = Base64.getDecoder().decode(movie.getCover());
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            Image image = new Image(bais);
            cover.setImage(image);

        }else{
            cover.setImage(null);
        }


    }

}

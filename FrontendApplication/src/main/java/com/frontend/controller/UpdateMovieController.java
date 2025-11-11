package com.frontend.controller;

import com.frontend.FrontendApplication;
import com.frontend.model.Movie;
import com.frontend.model.Requests;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.*;
import javafx.stage.FileChooser;


import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import org.apache.commons.io.FileUtils;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class UpdateMovieController extends MovielistController implements Initializable {

    @FXML
    private TextField tfTitle;
    @FXML
    private DatePicker datePickerReleasedDate;
    @FXML
    private MenuButton category;
    @FXML
    private TextField movieLength;
    @FXML
    private TextField director;
    @FXML
    private TextField writer;
    @FXML
    private TextField tfUpdateViewCast;
    @FXML
    private Label errorMessage;

    private String buildString;

    private Requests request = new Requests();

    private Gson gson = new Gson();

    private String json;
    private String cover;
    private Movie movie;

    public UpdateMovieController() {
    }

    public void addImage() throws IOException {
        FileChooser fileChooser = new FileChooser();
        File file = new File(fileChooser.showOpenDialog(null).getPath());
        byte[] bytes = FileUtils.readFileToByteArray(file);
        cover = Base64.getEncoder().encodeToString(bytes);
    }

    public String categoryStringBuilder() {


        StringBuilder stringBuilder = new StringBuilder();
        for (MenuItem menuItem : category.getItems()) {
            CustomMenuItem customMenuItem = (CustomMenuItem) menuItem;
            CheckBox checkBox = (CheckBox) customMenuItem.getContent();
            if (checkBox.isSelected()) {

                stringBuilder.append("," + checkBox.getText());


            }
        }
        if (stringBuilder.toString().equals("")) {
            return buildString;

        } else {
            stringBuilder.deleteCharAt(0);
            buildString = stringBuilder.toString();
            return buildString;
        }
    }


    public void updateMovie() throws IOException, InterruptedException {


        if(datePickerReleasedDate.getValue() == null){
            errorMessage.setText("Please enter a date. ");
            return;
        }else {

            movie.setTitle(tfTitle.getText());
            movie.setReleaseDate(datePickerReleasedDate.getValue().format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)));
            movie.setCategory(categoryStringBuilder());
            movie.setMovieLength(movieLength.getText());
            movie.setDirector(director.getText());
            movie.setWriter(writer.getText());
            movie.setCast(tfUpdateViewCast.getText());
            movie.setCover(cover);

            request.put("/movies/update/" + FrontendApplication.getSelectedMovie().getId(), gson.toJson(movie));
            switchToMovielist();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        for (MenuItem menuItem : category.getItems()) {
            CustomMenuItem customMenuItem = (CustomMenuItem) menuItem;
            customMenuItem.setHideOnClick(false);

        }

        try {
            request.get("/movies/" + FrontendApplication.getSelectedMovie().getId());
            movie = gson.fromJson(request.getMessage(), Movie.class);

            tfTitle.setText(movie.getTitle());
            buildString = movie.getCategory();
            movieLength.setText(movie.getMovieLength());
            director.setText(movie.getDirector());
            writer.setText(movie.getWriter());
            tfUpdateViewCast.setText(movie.getCast());
            cover = movie.getCover();


        } catch (Exception exc) {
            exc.printStackTrace();
        }

    }
}
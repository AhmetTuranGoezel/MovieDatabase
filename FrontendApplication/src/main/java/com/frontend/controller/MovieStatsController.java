package com.frontend.controller;

import com.frontend.FrontendApplication;
import com.frontend.model.Movie;
import com.frontend.model.Requests;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Delayed;

public class MovieStatsController extends SceneController implements Initializable {

    @FXML
    private Label averageR;

    @FXML
    private BarChart<Number, String> barChart;

    @FXML
    private Label numberR;

    @FXML
    private Label numberS;

    @FXML
    private Label title;

    @FXML
    private Label successfullyDownloadedMessage;


    private Requests request = new Requests();
    private Gson gson = new Gson();
    private Movie movie;


    public void resetStats() throws IOException, InterruptedException {
        request.delete("/movie/resetStats/" + movie.getId());
        switchToStatistics();
    }

    public void downloadStats() throws IOException{

        String movieName = movie.getTitle();
        if (movieName.contains(":") || movieName.contains("/") || movieName.contains("\"") || movieName.endsWith("?")
                || movieName.contains("!")) {

            movieName = movieName.replaceAll(":", " -");
            movieName = movieName.replaceAll("/", "_");
            movieName = movieName.replaceAll("\"", "'");
            movieName = movieName.replaceAll("\\?", "");
            movieName = movieName.replaceAll("!", "");
        }


        File statsFile = new File("StatsFor_" + movieName + ".txt");
        FileWriter writer = new FileWriter(statsFile);
        writer.write("Statistics for " + movieName + ":");
        writer.write(System.lineSeparator());
        writer.write(System.lineSeparator());
        writer.write("Total count of views:" + numberS.getText());
        writer.write(System.lineSeparator());
        writer.write("Total count of reviews:" + numberR.getText());
        writer.write(System.lineSeparator());
        writer.write("Average rating:" + averageR.getText());

        writer.close();
        successfullyDownloadedMessage.setText("Successfully downloaded! ");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {

            request.get("/movies/" +FrontendApplication.getSelectedMovie().getId());
            movie = gson.fromJson(request.getMessage(),Movie.class);

            String[] dateSplit = movie.getReleaseDate().split(" ");

            if(dateSplit.length == 3) {
                title.setText(movie.getTitle() + " (" + dateSplit[2] + ")");
            }else if(dateSplit.length == 2){
                title.setText(movie.getTitle() + " (" + dateSplit[1] + ")");
            }else if(dateSplit.length == 1){
                title.setText(movie.getTitle() + " (" + dateSplit[0] + ")");
            }
            numberS.setText(Integer.toString(movie.getViews()));
            numberR.setText(Integer.toString(movie.getReviewCount()));
            averageR.setText(Double.toString(movie.getAverageRating()));

            XYChart.Series<Number, String> s = new XYChart.Series<>();
            s.setName("Amount of rating");

            request.get("/reviews/movie/" + movie.getId() + "/rating/1");
            s.getData().add(new XYChart.Data<>(Integer.parseInt(request.getMessage()), "1"));
            request.get("/reviews/movie/" + movie.getId() + "/rating/2");
            s.getData().add(new XYChart.Data<>(Integer.parseInt(request.getMessage()), "2"));
            request.get("/reviews/movie/" + movie.getId() + "/rating/3");
            s.getData().add(new XYChart.Data<>(Integer.parseInt(request.getMessage()), "3"));
            request.get("/reviews/movie/" + movie.getId() + "/rating/4");
            s.getData().add(new XYChart.Data<>(Integer.parseInt(request.getMessage()), "4"));
            request.get("/reviews/movie/" + movie.getId() + "/rating/5");
            s.getData().add(new XYChart.Data<>(Integer.parseInt(request.getMessage()), "5"));
            barChart.getData().add(s);


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}

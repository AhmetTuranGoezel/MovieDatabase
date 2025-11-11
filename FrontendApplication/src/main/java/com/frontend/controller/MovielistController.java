package com.frontend.controller;

import com.frontend.FrontendApplication;
import com.frontend.model.*;
import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MovielistController extends SceneController implements Initializable {
    @FXML
    private TableView<Movie> table;
    @FXML
    private TableColumn<Movie, String> name;
    @FXML
    private TableColumn<Movie, String> erschienen;
    @FXML
    private TableColumn<Movie, CategoryEnum> kategorie;
    @FXML
    private TableColumn<Movie, String> cast;
    @FXML
    private Label labeltitle;
    @FXML
    private Label ldate;

    @FXML
    private Label lgenre;

    @FXML
    private Label llength;

    @FXML
    private Label ldirector;

    @FXML
    private Label lwriter;

    @FXML
    private Label lcast;

    @FXML
    private Label lreviews;

    @FXML
    private Pane pane;
    @FXML
    private MenuButton menuButton;
    @FXML
    private MenuButton add;
    @FXML
    private Label labellength;
    @FXML
    private Label labeldate;
    @FXML
    private Label labelgenre;
    @FXML
    private Label labeldirector;
    @FXML
    private Label labelwriter;
    @FXML
    private Label labelcast;
    @FXML
    private ImageView cover;
    @FXML
    private TextField search;
    @FXML
    private MenuButton category;
    @FXML
    private TextField tfcast;
    @FXML
    private TextField tfrelease;
    @FXML
    private TextField tftitle;
    @FXML
    private Label message;
    @FXML
    private VBox reviewOverview;

    public ArrayList<Movie> watchlist = new ArrayList<>();
    private String buildString;

    private Requests request = new Requests();
    private Gson gson = new Gson();


    public void openReportWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/report.fxml"));

        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));

        stage.centerOnScreen();
        stage.setResizable(false);
        stage.alwaysOnTopProperty();

        stage.show();
        FrontendApplication.setCurrentPopup(stage);
    }


    public ArrayList<Movie> getWatchlist() {
        return watchlist;
    }

    public void movieEdit() throws IOException, InterruptedException {
        if (FrontendApplication.getSelectedMovie() != null) {
            SwitchToUpdatemovie();
        }
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
    public boolean checkGenre(String x, String y){
        if(x.contains(",")) {
            String[] parts = x.split(",");
            boolean temp = false;
            for (int i = 0; i < parts.length; i++) {
                System.out.println(parts[i]);
                if (y.contains(parts[i])) {
                   temp = true;
                }else{
                    return false;
                }
            }
            return temp;
        }
        if(y.contains(x)){
            return true;
        }else{
            return false;
        }
    }


    public void addToWatchlist() {
        try {
            request.get("/movies/" + FrontendApplication.getSelectedMovie().getId());
            String movieJson = request.getMessage();
            request.post("/users/watchlist/" + FrontendApplication.getCurrentAccount().getAccountID(), movieJson);
            message.setText(request.getMessage());

            request.get("/users/" + FrontendApplication.getCurrentAccount().getAccountID());
            User user = gson.fromJson(request.getMessage(), User.class);
            FrontendApplication.setCurrentAccount(user);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private boolean findMovie(Movie movie, String text) {
        return (movie.getTitle().toLowerCase().contains(text.toLowerCase())) ||
                (movie.getReleaseDate().toLowerCase().contains(text.toLowerCase())) ||
                (movie.getCategory().toLowerCase().contains(text.toLowerCase())) ||
                (movie.getCast().toLowerCase().contains(text.toLowerCase()));
    }

    private ObservableList<Movie> filterMovies(List<Movie> list, String text) {
        List<Movie> filteredList = new ArrayList<>();
        for (Movie movie : list) {
            if (findMovie(movie, text)) filteredList.add(movie);
        }
        return FXCollections.observableList(filteredList);
    }

    public void filter() throws IOException, InterruptedException {
        name.setCellValueFactory(new PropertyValueFactory<Movie, String>("title"));
        erschienen.setCellValueFactory(new PropertyValueFactory<Movie, String>("releaseDate"));
        kategorie.setCellValueFactory(new PropertyValueFactory<Movie, CategoryEnum>("category"));
        cast.setCellValueFactory(new PropertyValueFactory<Movie, String>("cast"));
        table.setItems(filterManually());
    }


    public ObservableList<Movie> filterManually() throws IOException, InterruptedException {
        request.get("/movies/");
        String moviesAsJson = request.getMessage();

        Movie[] movies = gson.fromJson(moviesAsJson, Movie[].class);
        ArrayList<Movie> tempmovies = new ArrayList<>(Arrays.asList(movies));
        ArrayList<Movie> filteredList = new ArrayList<>();
        ObservableList<Movie> obsmovies = FXCollections.observableArrayList(tempmovies);

        if (categoryStringBuilder() != null || !tfcast.getText().isEmpty() || !tfrelease.getText().isEmpty() || !tftitle.getText().isEmpty()) {
            for (Movie movie : tempmovies) {
                Movie temp = new Movie();
                if (categoryStringBuilder() != null) {
                    temp.setCategory(categoryStringBuilder());
                }
                if (!tfcast.getText().isEmpty()) {
                    temp.setCast(tfcast.getText());
                }
                if (!tfrelease.getText().isEmpty()) {
                    temp.setReleaseDate(tfrelease.getText());
                }
                if (!tftitle.getText().isEmpty()) {
                    temp.setTitle(tftitle.getText());
                }
                if (findMovie(movie, temp.getTitle()) && findMovie(movie, temp.getCast()) && checkGenre(temp.getCategory(), movie.getCategory()) && findMovie(movie, temp.getReleaseDate())) {
                    filteredList.add(movie);
                }
            }

            ObservableList<Movie> observableList = FXCollections.observableArrayList(filteredList);
            return observableList;
        } else {
            return obsmovies;
        }
    }

    public void selectMovie(Movie movie){

        FrontendApplication.setSelectedMovie(movie);

        message.setText(null);
        String[] dateSplit = movie.getReleaseDate().split(" ");

        if(dateSplit.length == 3) {
            labeltitle.setText(movie.getTitle() + " (" + dateSplit[2] + ")");
        }else if(dateSplit.length == 2){
            labeltitle.setText(movie.getTitle() + " (" + dateSplit[1] + ")");
        }else if(dateSplit.length == 1){
            labeltitle.setText(movie.getTitle() + " (" + dateSplit[0] + ")");
        }

        ldate.setVisible(true);
        lgenre.setVisible(true);
        ldirector.setVisible(true);
        lwriter.setVisible(true);
        lcast.setVisible(true);
        llength.setVisible(true);
        pane.setVisible(true);
        lreviews.setVisible(true);
        labeldate.setText(movie.getReleaseDate());
        labelgenre.setText(movie.getCategory());
        labeldirector.setText(movie.getDirector());
        labelwriter.setText(movie.getWriter());
        labelcast.setText(movie.getCast());
        labellength.setText(movie.getMovieLength());

        if (FrontendApplication.getCurrentFxmlLoader().getLocation().equals(getClass().getResource("/views/movielistAdmin.fxml"))) {
            menuButton.setVisible(true);
        } else {
            add.setVisible(true);
        }

        try {
            request.get("/reviews/movie/" + movie.getId());
            String reviewsAsJson = request.getMessage();
            Review[] reviews = gson.fromJson(reviewsAsJson, Review[].class);

            reviewOverview.getChildren().clear();

            for (Review review : reviews) {

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/views/review.fxml"));
                HBox hBox = fxmlLoader.load();
                ReviewItemController reviewController = fxmlLoader.getController();
                reviewController.updateReview(review);
                reviewOverview.getChildren().add(hBox);
            }

            if (movie.getCover() != null) {
                    byte[] bytes = Base64.getDecoder().decode(movie.getCover());
                    ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                    BufferedImage bImage = ImageIO.read(bis);
                    Image image = SwingFXUtils.toFXImage(bImage, null);
                    cover.setImage(image);
                } else {
                    cover.setImage(null);
                }


        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



        //FrontendApplication.setSelectedMovie(null);
       // System.out.println(FrontendApplication.getCurrentAccount().getAccountID());
        try {
            request.get("/movies/");
            String moviesAsJson = request.getMessage();

            Movie[] movies = gson.fromJson(moviesAsJson, Movie[].class);
            ArrayList<Movie> tempmovies = new ArrayList<>(Arrays.asList(movies));

            ObservableList<Movie> obsmovies = FXCollections.observableArrayList(tempmovies);


            name.setCellValueFactory(new PropertyValueFactory<Movie, String>("title"));
            erschienen.setCellValueFactory(new PropertyValueFactory<Movie, String>("releaseDate"));
            kategorie.setCellValueFactory(new PropertyValueFactory<Movie, CategoryEnum>("category"));
            cast.setCellValueFactory(new PropertyValueFactory<Movie, String>("cast"));

            table.setItems(obsmovies);
            search.textProperty().addListener((observable, oldValue, newValue) ->
                    table.setItems(filterMovies(tempmovies, newValue)));


            table.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (table.getSelectionModel().getSelectedItem() != null) {
                        Movie temp = table.getSelectionModel().getSelectedItem();
                        selectMovie(temp);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

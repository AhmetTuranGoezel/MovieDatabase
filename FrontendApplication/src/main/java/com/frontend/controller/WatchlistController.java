package com.frontend.controller;

import com.frontend.FrontendApplication;
import com.frontend.model.Movie;
import com.frontend.model.Requests;
import com.frontend.model.Review;
import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.ResourceBundle;

public class WatchlistController extends SceneController implements Initializable {

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
    private Button delete;
    @FXML
    private Label labeldate;
    @FXML
    private Label labelgenre;
    @FXML
    private Label labellength;
    @FXML
    private Label labeldirector;
    @FXML
    private Label labelwriter;
    @FXML
    private Label labelcast;
    @FXML
    private TableView<Movie> table;
    @FXML
    private TableColumn<Movie, String> title;
    @FXML
    private ImageView cover;
    @FXML
    private VBox reviewOverview;

    Requests req = new Requests();
    Gson gson = new Gson();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FrontendApplication.setSelectedMovie(null);

        try {
            req.get("/users/watchlist/" + FrontendApplication.getCurrentAccount().getAccountID());
            String moviesAsJson = req.getMessage();

            Movie[] movies = gson.fromJson(moviesAsJson, Movie[].class);
            ArrayList<Movie> tempmovies = new ArrayList<>(Arrays.asList(movies));
            ObservableList<Movie> obsmovies = FXCollections.observableArrayList(tempmovies);
            title.setCellValueFactory(new PropertyValueFactory<Movie, String>("title"));
            table.setItems(obsmovies);
            table.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (table.getSelectionModel().getSelectedItem() != null) {
                        Movie temp = table.getSelectionModel().getSelectedItem();
                        labeltitle.setText(temp.getTitle());

                        ldate.setVisible(true);
                        lgenre.setVisible(true);
                        ldirector.setVisible(true);
                        lwriter.setVisible(true);
                        lcast.setVisible(true);
                        llength.setVisible(true);
                        delete.setVisible(true);
                        pane.setVisible(true);
                        lreviews.setVisible(true);

                        labeldate.setText(temp.getReleaseDate());
                        labelgenre.setText(temp.getCategory());
                        labeldirector.setText(temp.getDirector());
                        labelwriter.setText(temp.getWriter());
                        labelcast.setText(temp.getCast());
                        labellength.setText(temp.getMovieLength());
                        FrontendApplication.setSelectedMovie(temp);

                        try {
                            req.get("/reviews/movie/" + FrontendApplication.getSelectedMovie().getId());
                            String reviewsAsJson = req.getMessage();
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
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }

                        try {
                            if (temp.getCover() != null) {
                                byte[] bytes = Base64.getDecoder().decode(temp.getCover());
                                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                                BufferedImage bImage = ImageIO.read(bis);
                                Image image = SwingFXUtils.toFXImage(bImage, null);
                                cover.setImage(image);
                            } else {
                                cover.setImage(null);

                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void seen() {
        try {
            req.get("/movies/" + FrontendApplication.getSelectedMovie().getId());
            String movieJson = req.getMessage();
            req.delete("/movies/delete/watchlist/" + FrontendApplication.getCurrentAccount().getAccountID() + "/"
                    + FrontendApplication.getSelectedMovie().getId());
            req.post("/users/history/" + FrontendApplication.getCurrentAccount().getAccountID(), movieJson);
            switchToWatchList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

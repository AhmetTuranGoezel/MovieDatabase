package com.frontend.controller;

import com.frontend.FrontendApplication;
import com.frontend.model.*;
import com.google.gson.Gson;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.ResourceBundle;

public class StartController extends SceneController implements Initializable {
    @FXML
    private ImageView cover;
    @FXML
    private MenuButton add;
    @FXML
    private HBox historyOverview;

    @FXML
    private Label labelcast;

    @FXML
    private Label labeldate;

    @FXML
    private Label labeldirector;

    @FXML
    private Label labelgenre;

    @FXML
    private Label labellength;

    @FXML
    private Label labeltitle;

    @FXML
    private Label labelwriter;

    @FXML
    private Label lcast;

    @FXML
    private Label ldate;

    @FXML
    private Label ldirector;

    @FXML
    private Label lgenre;

    @FXML
    private Label llength;

    @FXML
    private Label lreviews;

    @FXML
    private Label lwriter;

    @FXML
    private Pane pane;

    @FXML
    private Label message;

    @FXML
    private MenuItem historyButton;

    @FXML
    private MenuItem reviewsButton;

    @FXML
    private MenuItem watchlistButton;
    @FXML
    private MenuItem invitationButton;
    @FXML
    private MenuItem reportButton;

    @FXML
    private HBox recommendationsOverview;

    @FXML
    private VBox reviewOverview;

    @FXML
    private HBox watchlistOverview;

    @FXML
    private VBox notificationOverview;
    @FXML
    private ScrollPane scrollpaneNotifications;

    private Requests request = new Requests();

    private Gson gson = new Gson();


    public void showDetails(Movie movie) {

        add.setVisible(true);


        String[] dateSplit = movie.getReleaseDate().split(" ");

        if (dateSplit.length == 3) {
            labeltitle.setText(movie.getTitle() + " (" + dateSplit[2] + ")");
        } else if (dateSplit.length == 2) {
            labeltitle.setText(movie.getTitle() + " (" + dateSplit[1] + ")");
        } else if (dateSplit.length == 1) {
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
        FrontendApplication.setSelectedMovie(movie);

        try {
            request.get("/reviews/movie/" + FrontendApplication.getSelectedMovie().getId());
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
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        try {
            if (movie.getCover() != null) {
                byte[] bytes = Base64.getDecoder().decode(movie.getCover());
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

    public void seen() {

        HistoryMovie historyMovie = new HistoryMovie(FrontendApplication.getSelectedMovie());
        try {
            request.delete("/movies/delete/watchlist/" + FrontendApplication.getCurrentAccount().getAccountID() + "/"
                    + FrontendApplication.getSelectedMovie().getId());
            request.post("/users/history/" + FrontendApplication.getCurrentAccount().getAccountID(), gson.toJson(historyMovie));
            switchToStart();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addReview() throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/views/addReview.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        FrontendApplication.setCurrentPopup(stage);
    }

    public void openReportWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/report.fxml"));

        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));

        stage.centerOnScreen();
        stage.setResizable(false);
        stage.alwaysOnTopProperty();
        stage.setOnCloseRequest(e -> {
            try {
                switchToStart();
            } catch (IOException | InterruptedException exc) {
                exc.printStackTrace();
            }
        });
        stage.show();

        FrontendApplication.setCurrentPopup(stage);
    }

    public void openInvitationWindow() throws IOException, InterruptedException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/invitePerson.fxml"));
        FrontendApplication.setCurrentFxmlLoader(loader);
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));

        stage.centerOnScreen();
        stage.setResizable(false);
        stage.alwaysOnTopProperty();
        stage.setOnCloseRequest(e -> {
            try {
                switchToStart();
            } catch (IOException | InterruptedException exc) {
                exc.printStackTrace();
            }
        });

        stage.show();
        FrontendApplication.setCurrentPopup(stage);
        FrontendApplication.updateCurrentAccount();
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
            switchToStart();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean recommend(Movie movie, User user) throws IOException, InterruptedException {
        if (validate(movie, user) == true) {
            return true;
        } else {
            request.get("/users/friends/" + user.getAccountID());
            String friendsJson = request.getMessage();
            User[] friends = gson.fromJson(friendsJson, User[].class);
            if (friends.length != 0) {

                for (User u : friends) {
                    if (validate(movie, u)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean validate(Movie movie, User user) throws IOException, InterruptedException {
        request.get("/reviews/user/" + user.getAccountID());
        String reviewsJson = request.getMessage();
        Review[] reviews = gson.fromJson(reviewsJson, Review[].class);

        if (reviews.length != 0) {
            if (reviews.length > 10) {
                for (int i = reviews.length - 1; i >= reviews.length - 11; i--) {
                    Review temp = reviews[i];
                    if (temp.getRating() >= 3 && checkGenre(movie.getCategory(), temp.getMovie().getCategory()) == true) {
                        return true;
                    }
                }
            } else {
                for (int i = 0; i < reviews.length; i++) {
                    Review temp = reviews[i];
                    if (temp.getRating() >= 3 && checkGenre(movie.getCategory(), temp.getMovie().getCategory())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean checkGenre(String x, String y) {
        if (x.contains(",")) {
            String[] parts = x.split(",");
            int z = 0;
            for (int i = 0; i < parts.length; i++) {
                if (y.contains(parts[i])) {
                    z++;
                }
            }
            if(!y.contains(",")){
                if(z >= 1){
                    return true;
                }
            }else if (y.contains(",") && z >= 2) {
                return true;
            } else {
                return false;
            }
        }
        if (y.contains(x)) {
            return true;
        } else {
            return false;
        }
    }


    public void showOrHideNotifications() throws IOException, InterruptedException {

        if (notificationOverview.isVisible()) {
            notificationOverview.setVisible(false);
            scrollpaneNotifications.setVisible(false);
        } else {

            notificationOverview.getChildren().clear();
            request.get("/notifications/" + FrontendApplication.getCurrentAccount().getAccountID());
            Notification[] notifications = gson.fromJson(request.getMessage(), Notification[].class);


            for (Notification n : notifications) {

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/views/notificationItem.fxml"));
                HBox hBox = fxmlLoader.load();
                NotificationItemController controller = fxmlLoader.getController();
                controller.setNotification(n);
                notificationOverview.getChildren().add(hBox);


                notificationOverview.setVisible(true);
                scrollpaneNotifications.setVisible(true);
            }
        }
    }

    public void setVisibilityHistoryButton(boolean boo) {
        historyButton.setVisible(boo);
    }

    public void setVisibilityWatchlistButton(boolean boo) {
        watchlistButton.setVisible(boo);
    }

    public void setVisibilityReviewButton(boolean boo) {
        reviewsButton.setVisible(boo);
    }

    public void setVisibilityInvitationButton(boolean boo) {
        invitationButton.setVisible(boo);
    }

    public void setVisibilityReportButton(boolean boo) {
        reportButton.setVisible(boo);
    }


    public void resetButtons(){
        invitationButton.setVisible(false);
        historyButton.setVisible(false);
        watchlistButton.setVisible(false);
        reviewsButton.setVisible(false);
        reportButton.setVisible(false);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {

            request.get("/users/watchlist/" + FrontendApplication.getCurrentAccount().getAccountID());
            String moviesAsJson = request.getMessage();

            Movie[] movies = gson.fromJson(moviesAsJson, Movie[].class);

            for (Movie movie : movies) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/views/watchlistItem.fxml"));
                HBox hBox = fxmlLoader.load();
                MovieItemController movieItemController = fxmlLoader.getController();
                movieItemController.setMovie(movie);
                watchlistOverview.getChildren().add(hBox);

            }
            request.get("/movies/");
            String movieJson = request.getMessage();
            Movie[] movieArray = gson.fromJson(movieJson, Movie[].class);

            request.get("/users/history/" + FrontendApplication.getCurrentAccount().getAccountID());
            String historyJson = request.getMessage();
            HistoryMovie[] historyArray = gson.fromJson(historyJson, HistoryMovie[].class);

            ArrayList<Movie> newMovies = new ArrayList<>();
            ArrayList<Movie> recMovies = new ArrayList<>();

            for (Movie movie : movieArray) {
                boolean temp = false;
                for (Movie wlmovie : movies) {
                    if (movie.getId() == wlmovie.getId()) {
                        temp = true;
                        break;
                    }
                }
                for (HistoryMovie hmovie : historyArray) {
                    if (movie.getId() == hmovie.getMovie().getId()) {
                        temp = true;
                        break;
                    }
                }
                // System.out.println(temp);
                if (temp == false) {
                    newMovies.add(movie);
                }
            }
            Collections.shuffle(newMovies);
            int temp = 0;
            for (Movie movie : newMovies) {
                if (recommend(movie, (User)FrontendApplication.getCurrentAccount()) == true) {
                    recMovies.add(movie);
                    temp++;
                }
                if (temp == 15) {
                    break;
                }
            }

            for (Movie movie : recMovies) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/views/recommendationItem.fxml"));
                HBox hBox = fxmlLoader.load();
                MovieItemController movieItemController = fxmlLoader.getController();
                movieItemController.setMovie(movie);
                recommendationsOverview.getChildren().add(hBox);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            request.get("/users/history/" + FrontendApplication.getCurrentAccount().getAccountID());
            String moviesAsJson = request.getMessage();

            HistoryMovie[] movies = gson.fromJson(moviesAsJson, HistoryMovie[].class);

            for (HistoryMovie movie : movies) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/views/historyItem.fxml"));
                HBox hBox = fxmlLoader.load();
                MovieItemController movieItemController = fxmlLoader.getController();
                movieItemController.setMovie(movie.getMovie());
                historyOverview.getChildren().add(hBox);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}

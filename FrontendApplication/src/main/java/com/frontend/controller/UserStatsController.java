package com.frontend.controller;

import com.frontend.FrontendApplication;
import com.frontend.model.HistoryMovie;
import com.frontend.model.Movie;
import com.frontend.model.Requests;
import com.frontend.model.Review;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class UserStatsController extends SceneController implements Initializable {


    @FXML
    private DatePicker datePickerStart;
    @FXML
    private DatePicker datePickerEnd;
    @FXML
    private VBox favactorOverview;

    @FXML
    private VBox favgenreOverview;

    @FXML
    private VBox favmovieOverview;

    @FXML
    private Label spendingTime;

    private Requests request = new Requests();
    private Gson gson = new Gson();
    private StatisticsItemController sic;
    private List<String> actorsList = new LinkedList<>();
    private List<String> genreList = new LinkedList<>();
    private List<Review> reviewList = new LinkedList<>();
    private int userWatchtime;

    public List<VBox> getFavItems(List<String> list) throws IOException {


        List<VBox> favItems = new LinkedList<>();
        Set<String> occuringItems = new HashSet<>(list);


        int highestFreq = 0;
        String fav = "";

        for (int i = 1; i < 4; i++) {

            for (String item:occuringItems) {
                if (Collections.frequency(list, item) > highestFreq) {
                    highestFreq = Collections.frequency(list, item);
                    fav = item;

                }
            }
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/views/userStatsItem.fxml"));
            VBox vBox = fxmlLoader.load();
            sic = fxmlLoader.getController();
            sic.setRank(i);
            sic.setName(fav);
            favItems.add(vBox);

            occuringItems.remove(fav);
            highestFreq = 0;

        }
        return favItems;
    }


    public List<VBox> getFavMovies() throws IOException, InterruptedException {

        List<VBox> favMovies = new LinkedList<>();

        request.get("/reviews/user/" + FrontendApplication.getCurrentAccount().getAccountID() + "/date/"
                + datePickerStart.getValue().toString() + "/" + datePickerEnd.getValue().toString());

        Review[] reviews = gson.fromJson(request.getMessage(), Review[].class);


            for (Review r : reviews) {
                reviewList.add(r);
            }


            if(!reviewList.isEmpty()){
            int highestRating = 0;
            Review favMovie = new Review();

            for (int i = 1; i < 4; i++) {
                for (Review review : reviewList) {

                    System.out.println(review.getMovie().getTitle());
                    if (review.getRating() > highestRating) {
                        favMovie = review;
                        highestRating = review.getRating();
                    }
                }

                if(favMovie != null) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/views/userStatsItem.fxml"));
                    VBox vBox = fxmlLoader.load();
                    sic = fxmlLoader.getController();
                    sic.setRank(i);
                    sic.setName(favMovie.getMovie().getTitle());
                    favMovies.add(vBox);


                    reviewList.remove(favMovie);
                    favMovie = null;
                    highestRating = 0;
                }
            }
        }else{
                for(int i=1; i<4; i++) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/views/userStatsItem.fxml"));
                    VBox vBox = fxmlLoader.load();
                    sic = fxmlLoader.getController();
                    sic.setRank(i);
                    sic.setName("");
                    favMovies.add(vBox);
                }
            }
        return favMovies;
    }

    public void showStats() throws IOException, InterruptedException {

        userWatchtime = 0;
        actorsList.removeAll(actorsList);
        genreList.removeAll(genreList);
        reviewList.removeAll(reviewList);

        spendingTime.setText("0 min");
        favactorOverview.getChildren().clear();
        favgenreOverview.getChildren().clear();
        favmovieOverview.getChildren().clear();

        if (datePickerStart.getValue() == null){
            datePickerStart.setValue(LocalDate.of(2022,01,01));
        }
        if(datePickerEnd.getValue() == null){
            datePickerEnd.setValue(LocalDate.of(2022,12,31));
        }

        request.get("/users/history/" + FrontendApplication.getCurrentAccount().getAccountID());
        HistoryMovie[] movies = gson.fromJson(request.getMessage(), HistoryMovie[].class);

        LocalDate addedToHistory;


        for (HistoryMovie movie : movies) {

            addedToHistory = LocalDate.parse(movie.getDateAdded());
            if ((datePickerStart.getValue().isBefore(addedToHistory) || datePickerStart.getValue().isEqual(addedToHistory)) &&
                    (datePickerEnd.getValue().isEqual(addedToHistory) || datePickerEnd.getValue().isAfter(addedToHistory))) {


                if(movie.getMovie().getMovieLength() != null) {
                    String length = movie.getMovie().getMovieLength().replace(" min", "");
                    System.out.println(Integer.parseInt(length));
                    userWatchtime += Integer.parseInt(length);
                }
                spendingTime.setText(userWatchtime + " min");


                String[] actors = movie.getMovie().getCast().split(",");
                for (int i = 0; i < actors.length; i++) {
                    if (actors[i].contains("(")) {
                        String brackets = actors[i].substring(actors[i].indexOf("("), actors[i].indexOf(")") + 1);
                        actors[i] = actors[i].replace(brackets, "");
                    }

                    if (actors[i].startsWith(" ")) {
                        actors[i] = actors[i].substring(1, actors[i].length());
                    }

                    actorsList.add(actors[i]);
                }

                //System.out.println(actors[0]);

                String[] genres = movie.getMovie().getCategory().split(", ");

                /*for(String s:genres){
                    genreList.add(s);
                }*/
                genreList.addAll(List.of(genres));
            }
        }
                favactorOverview.getChildren().addAll(getFavItems(actorsList));
                favgenreOverview.getChildren().addAll(getFavItems(genreList));
                favmovieOverview.getChildren().addAll(getFavMovies());


    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {

            datePickerStart.setValue(LocalDate.of(2022, 01, 01));
            datePickerEnd.setValue(LocalDate.of(2022, 12, 31));
            showStats();

        }
        catch (Exception e){
            e.printStackTrace();
        }
         /* String No1 = hey.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                    .entrySet().stream().max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey).orElse(null);

        System.out.println(No1);

        String No2 = hey.stream().filter(x -> !x.equals(No1)).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                    .entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(null);

        System.out.println(No2);

        String No3 = hey.stream().filter(x -> !x.equals(No1) && !x.equals(No2)).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                    .entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(null);

        System.out.println(No3);*/
    }
}

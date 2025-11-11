package com.frontend.controller;

import com.frontend.FrontendApplication;
import com.frontend.model.*;
import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.ResourceBundle;

public class ReportController extends SceneController implements Initializable {

    @FXML
    private Label cast;

    @FXML
    private Label directors;

    @FXML
    private Label movieGenre;

    @FXML
    private Label movieLength;

    @FXML
    private Label movieTitle;
    @FXML
    private ImageView cover;

    @FXML
    private Label releaseDate;

    @FXML
    private TextArea reporttext;

    @FXML
    private Label writers;

    @FXML
    private TableView<Report> tableReports;
    @FXML
    private TableColumn<Report, Integer> columnReportId;
    @FXML
    private TableColumn<Report, String> columnEmail;

    @FXML
    private TableColumn<Report, String> columnMovieTitle;

    @FXML
    private TableColumn<Report, String> columnMovieId;

    @FXML
    private TableColumn<Report, String> columnUsername;

    @FXML
    private Label labelMovieTitle;

    @FXML
    private Label labelReport;

    @FXML
    private Text reportMessage;
    private Requests request = new Requests();
    private Gson gson = new Gson();
    private Movie movie = new Movie();
    private Report report = new Report();
    private Notification notification = new Notification();

    public void sendReport() throws IOException, InterruptedException {
        Report report = new Report();
        report.setText(reporttext.getText());
        report.setMovie(movie);
        report.setUser((User) FrontendApplication.getCurrentAccount());
        request.post("/reports/create/", gson.toJson(report));
        FrontendApplication.getCurrentPopup().close();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Successful report");
        alert.setContentText("Your report has been successfully submitted");
        alert.show();

    }

    public void reportDone() {

        try {
            request.delete("/reports/movie/" + report.getReportId() + "/"
                    + report.getUser().getAccountID() + "/" + report.getMovie().getId()
                    + "/" + FrontendApplication.getCurrentAccount().getAccountID());

            notification.setType(Notification.Type.Report);
            notification.setText(null, Notification.Type.Report,report.getMovie());
            notification.setNotificatedUser(report.getUser());
            notification.setNotificationImg(Notification.Type.Report);
            notification.setMovie(report.getMovie());

            request.post("/notification/create/",gson.toJson(notification));

            SwitchToReport();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            FrontendApplication.updateCurrentAccount();

        } catch (IOException | InterruptedException exc) {
            exc.printStackTrace();
        }

        try {
            if (FrontendApplication.getCurrentAccount().isAdmin()) {

                request.get("/reports/");
                String reportsAsJson = request.getMessage();

                Report[] reports = gson.fromJson(reportsAsJson, Report[].class);

                ArrayList<Report> reportArrayList = new ArrayList<>(Arrays.asList(reports));
                ObservableList<Report> reportObservableList = FXCollections.observableArrayList(reportArrayList);

                for (Report r : reportObservableList) {
                    r.setUsername(r.getUser());
                    r.setUserEmail(r.getUser());
                    r.setMovieTitle(r.getMovie());
                    r.setMovieId(r.getMovie());
                }

                columnReportId.setCellValueFactory(new PropertyValueFactory<Report, Integer>("reportId"));
                columnUsername.setCellValueFactory(new PropertyValueFactory<Report, String>("username"));
                columnEmail.setCellValueFactory(new PropertyValueFactory<Report, String>("userEmail"));
                columnMovieTitle.setCellValueFactory(new PropertyValueFactory<Report, String>("movieTitle"));
                columnMovieId.setCellValueFactory(new PropertyValueFactory<Report, String>("movieId"));

                tableReports.setItems(reportObservableList);

                tableReports.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (tableReports.getSelectionModel().getSelectedItem() != null) {
                            report = tableReports.getSelectionModel().getSelectedItem();
                            labelReport.setText("Report No. " + report.getReportId() + ":");
                            labelMovieTitle.setText(report.getMovieTitle());
                            reportMessage.setText(report.getText());

                            FrontendApplication.setSelectedReport(report);

                        }
                    }
                });


            } else {

                movie = FrontendApplication.getSelectedMovie();
                movieTitle.setText(movie.getTitle());

                byte[] bytes = Base64.getDecoder().decode(movie.getCover());
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                Image img = new Image(bais);

                cover.setImage(img);
                releaseDate.setText(movie.getReleaseDate());
                movieLength.setText(movie.getMovieLength());
                movieGenre.setText(movie.getCategory());
                directors.setText(movie.getDirector());
                writers.setText(movie.getWriter());
                cast.setText(movie.getCast());

            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

}

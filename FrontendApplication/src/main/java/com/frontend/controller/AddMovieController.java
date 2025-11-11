package com.frontend.controller;

import com.frontend.model.CategoryEnum;
import com.frontend.model.Movie;
import com.frontend.model.Requests;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;


public class AddMovieController extends SceneController implements Initializable {


    @FXML
    private TextField title;
    @FXML
    private DatePicker datePickerReleaseDate;
    @FXML
    private MenuButton category;
    @FXML
    private TextField movieLength;
    @FXML
    private TextField director;
    @FXML
    private TextField writer;
    @FXML
    private TextField cast;
    private String cover;
    @FXML
    private ComboBox aCategory;
    @FXML
    private ComboBox aReleaseYear;
    @FXML
    private ComboBox aReleaseYear2;
    @FXML
    private Label errormsg;

    private String buildString;

    private Requests request = new Requests();

    private Gson gson = new Gson();

    private String json;

    private WebClient wc = new WebClient();
    private HtmlPage page;


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


    public void addManually() throws IOException {


        Movie movie = new Movie(title.getText(), datePickerReleaseDate.getValue().format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)),
                categoryStringBuilder(), movieLength.getText(),
                director.getText(), writer.getText(),
                cast.getText(), cover);


        json = gson.toJson(movie);

        try {
            request.post("/movies/create/", json);

            if (request.getStatus().is2xxSuccessful()) {
                switchToMovielist();
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }


    public LocalDate setReleaseDate(String releaseYear, String releaseMonth, String releaseDay){
        LocalDate date = null;

        int year = Integer.parseInt(releaseYear);
        int day = Integer.parseInt(releaseDay);

        switch(releaseMonth){
            case "January":
                date = LocalDate.of(year,Month.JANUARY, day);
                break;
            case "February":
                date = LocalDate.of(year,Month.FEBRUARY, day);
                break;
            case "March":
                date = LocalDate.of(year,Month.MARCH, day);
                break;
            case "April":
                date = LocalDate.of(year,Month.APRIL, day);
                break;
            case "May":
                date = LocalDate.of(year,Month.MAY, day);
                break;
            case "June":
                date = LocalDate.of(year,Month.JUNE, day);
                break;
            case "July":
                date = LocalDate.of(year,Month.JULY, day);
                break;
            case "August":
                date = LocalDate.of(year,Month.AUGUST, day);
                break;
            case "September":
                date = LocalDate.of(year,Month.SEPTEMBER, day);
                break;
            case "October":
                date = LocalDate.of(year,Month.OCTOBER, day);
                break;
            case "November":
                date = LocalDate.of(year,Month.NOVEMBER, day);
                break;
            case "December":
                date = LocalDate.of(year,Month.DECEMBER, day);
                break;
        }

        return date;
    }

    public void addAutomatically() {


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    wc.getOptions().setCssEnabled(false);
                    wc.getOptions().setJavaScriptEnabled(false);

                    String dayMonthStart = "-01-01";
                    String dayMonthEnd = "-12-31";

                    if (aCategory.getValue() != null || aReleaseYear.getValue() != null || aReleaseYear2.getValue() != null) {

                                if (aCategory.getValue() == null) {
                                    aCategory.setValue("all");
                                }
                                if (aReleaseYear.getValue() == null) {
                                    aReleaseYear.setValue(2000);

                                }
                                if (aReleaseYear2.getValue() == null) {
                                    aReleaseYear2.setValue(2022);

                                }

                                if ((int) aReleaseYear.getValue() > (int) aReleaseYear2.getValue()) {
                                    errormsg.setText("Startyear must be smaller than or equal to endyear!");
                                    return;
                                }


                        if(aReleaseYear2.getValue().equals(2022)){
                            dayMonthEnd = "-06-15";

                        }
                        System.out.println(dayMonthEnd);

                        for (int p = 1; p < 5000; p += 250) {
                            page = wc.getPage("https://www.imdb.com/search/title/?title_type=feature,tv_movie&release_date="
                                    + aReleaseYear.getValue().toString() + dayMonthStart + "," + aReleaseYear2.getValue().toString()
                                    + dayMonthEnd + "&genres=" + aCategory.getValue().toString().toLowerCase()
                                    + "&count=250&start=" + p + "&ref_=adv_nxt");


                            List<HtmlDivision> movieList = page.getByXPath("//div[@class='lister-item-content']");


                            for (HtmlDivision div : movieList) {

                                Movie movie = new Movie();
                                HtmlElement movieName = (HtmlElement) div.getByXPath("h3/a").get(0);
                                movie.setTitle(movieName.getTextContent());


                                if (div.getByXPath("p[1]/span[@class='genre']").size() != 0) {
                                    HtmlElement movieGenre = (HtmlElement) div.getByXPath("p[1]/span[@class='genre']").get(0);
                                    movie.setCategory(movieGenre.asNormalizedText());
                                }

                                if (div.getByXPath("p[1]/span[@class='runtime']").size() != 0) {
                                    HtmlElement movieLength = (HtmlElement) div.getByXPath("p[1]/span[@class='runtime']").get(0);
                                    movie.setMovieLength(movieLength.asNormalizedText());
                                }

                                HtmlPage movieDetailsPage = wc.getPage("https://www.imdb.com" + movieName.getAttribute("href"));

                                if (movieDetailsPage.getByXPath("//ul[@data-testid='hero-title-block__metadata']").size() != 0) {
                                    HtmlUnorderedList ulWithMovieRelease = (HtmlUnorderedList) movieDetailsPage.getByXPath("//ul[@data-testid='hero-title-block__metadata']").get(0);
                                    List<HtmlElement> listWithMovieRelease = ulWithMovieRelease.getByXPath("//a[@href]");

                                    for (HtmlElement element : listWithMovieRelease) {
                                        if (element.getAttribute("href").contains("releaseinfo")) {
                                            HtmlPage releaseInfoPage = wc.getPage("https://www.imdb.com" + element.getAttributes().getNamedItem("href").getNodeValue());
                                            HtmlTable releaseInfoTable = (HtmlTable) releaseInfoPage.getByXPath("//table[@class='ipl-zebra-list ipl-zebra-list--fixed-first release-dates-table-test-only']").get(0);

                                            if (releaseInfoTable.getRows().isEmpty()) {
                                                movie.setReleaseDate(element.asNormalizedText());
                                            } else {
                                                for (HtmlTableRow row : releaseInfoTable.getRows()) {

                                                    String[] dateLines = row.getCell(1).asNormalizedText().split("\\s+");
                                                    if (dateLines.length == 3) {

                                                        LocalDate releaseDate = setReleaseDate(dateLines[2], dateLines[1], dateLines[0]);

                                                        if(releaseDate != null) {
                                                            movie.setReleaseDate(releaseDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)));
                                                            break;
                                                        }
                                                    }
                                                }
                                                if (movie.getReleaseDate() == null) {
                                                    for (HtmlTableRow row : releaseInfoTable.getRows()) {

                                                        String[] dateLines = row.getCell(1).asNormalizedText().split("\\s+");
                                                        if (dateLines.length == 2) {

                                                            LocalDate releaseDate = setReleaseDate(dateLines[2], dateLines[1], "1");


                                                            if(releaseDate != null) {
                                                                movie.setReleaseDate(releaseDate.format(DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH)));
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                                if (movie.getReleaseDate() == null) {
                                                    movie.setReleaseDate(element.asNormalizedText());
                                                }
                                            }
                                        }
                                    }
                                }


                                if (movieDetailsPage.getByXPath("//img[@class='ipc-image']").size() != 0) {
                                    HtmlImage image = (HtmlImage) movieDetailsPage.getByXPath("//img[@class='ipc-image']").get(0);
                                    URL imageURL = new URL(image.getAttribute("src"));
                                    BufferedImage movieImage = ImageIO.read(imageURL);

                                    String fileName = movie.getTitle();
                                    if (fileName.contains(":") || fileName.contains("/") || fileName.contains("\"") || fileName.endsWith("?")
                                            || fileName.contains("!")) {

                                        fileName = fileName.replaceAll(":", " -");
                                        fileName = fileName.replaceAll("/", "_");
                                        fileName = fileName.replaceAll("\"", "'");
                                        fileName = fileName.replaceAll("\\?", "");
                                        fileName = fileName.replaceAll("!", "");
                                    }

                                    File file = new File(fileName + ".jpg");
                                    ImageIO.write(movieImage, "jpg", file);
                                    byte[] bytes = FileUtils.readFileToByteArray(file);
                                    movie.setCover(Base64.getEncoder().encodeToString(bytes));
                                }

                                if (movieDetailsPage.getByXPath("//ul[@class='ipc-metadata-list ipc-metadata-list--dividers-all title-pc-list ipc-metadata-list--baseAlt']").size() != 0) {

                                    HtmlUnorderedList dwcUL = (HtmlUnorderedList) movieDetailsPage.getByXPath("//ul[@class='ipc-metadata-list ipc-metadata-list--dividers-all title-pc-list ipc-metadata-list--baseAlt']").get(0);
                                    List<HtmlListItem> dwcList = dwcUL.getByXPath("li");
                                    String[] lines;
                                    for (HtmlListItem item : dwcList) {

                                        if (item.asNormalizedText().contains("Director")) {
                                            lines = item.asNormalizedText().split("\n");
                                            String directors = "";

                                            for (int i = 1; i < lines.length; i++) {
                                                if (directors.equals("")) {
                                                    directors = lines[i];
                                                } else
                                                    directors += ", " + lines[i];
                                            }
                                            movie.setDirector(directors);
                                        } else if (item.asNormalizedText().contains("Writer")) {
                                            lines = item.asNormalizedText().split("\n");
                                            String writers = "";

                                            for (int i = 1; i < lines.length; i++) {
                                                if (writers.equals("")) {
                                                    writers = lines[i];
                                                } else
                                                    writers += ", " + lines[i];
                                            }
                                            movie.setWriter(writers);
                                        } else if (item.asNormalizedText().contains("Star")) {
                                            lines = item.asNormalizedText().split("\n");
                                            String cast = "";

                                            for (int i = 1; i < lines.length; i++) {
                                                if (cast.equals("")) {
                                                    cast = lines[i];
                                                } else
                                                    cast += ", " + lines[i];
                                            }
                                            movie.setCast(cast);
                                        }

                                    }
                                }
                                //System.out.println(gson.toJson(movie));

                                try {
                                    request.post("/movies/create/", gson.toJson(movie));


                                } catch (Exception exc) {
                                    exc.printStackTrace();
                                }
                                if (request.getStatus().is4xxClientError()) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setContentText(request.getMessage());
                                    alert.setResizable(false);
                                    alert.show();
                                }
                            }
                        }

                        switchToMovielist();
                    } else {

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                errormsg.setText("Please check your input");
                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (MenuItem menuItem : category.getItems()) {
            CustomMenuItem customMenuItem = (CustomMenuItem) menuItem;
            customMenuItem.setHideOnClick(false);

        }

        aCategory.getItems().setAll(CategoryEnum.values());

        for (int i = 2000; i < 2023; i++) {
            aReleaseYear.getItems().add(i);
            aReleaseYear2.getItems().add(i);

        }
    }
}
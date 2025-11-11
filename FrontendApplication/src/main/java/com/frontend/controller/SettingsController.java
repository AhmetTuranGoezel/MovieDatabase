package com.frontend.controller;

import com.frontend.FrontendApplication;
import com.frontend.model.Requests;
import com.frontend.model.User;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class SettingsController extends SceneController implements Initializable {

    @FXML
    private TextField usernametf;
    @FXML
    private TextField fnametf;
    @FXML
    private TextField lnametf;
    @FXML
    private TextField emailtf;
    @FXML
    private TextField currentpw;
    @FXML
    private TextField newpw;
    @FXML
    private TextField repeatpw;
    @FXML
    private ImageView imageview;
    @FXML
    private MenuButton friendslistpv;
    @FXML
    private MenuButton historypv;
    @FXML
    private MenuButton reviewpv;
    @FXML
    private MenuButton watchlistpv;
    @FXML
    private Label mandatory1;

    @FXML
    private Label mandatory2;
    @FXML
    private Label errorEmailMessage;


    @FXML
    private Label mandatory3;
    @FXML
    private Label wrongPassword;

    private User temp;
    private String profileImg;
    private int privacyWl;
    private int privacyHis;
    private int privacyFl;
    private int privacyRev;
    private Requests request = new Requests();
    private Gson gson = new Gson();


    public void addImage() throws IOException {
        FileChooser fileChooser = new FileChooser();
        File file = new File(fileChooser.showOpenDialog(null).getPath());
        byte[] bytes = FileUtils.readFileToByteArray(file);
        profileImg = Base64.getEncoder().encodeToString(bytes);


        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        Image image = new Image(bais);
        imageview.setImage(image);

    }

    public void deleteImage() throws IOException {

        byte[] byteArray = IOUtils.toByteArray(getClass().getResourceAsStream("/profile.png"));
        profileImg = Base64.getEncoder().encodeToString(byteArray);

        ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
        Image image = new Image(bais);
        imageview.setImage(image);

    }

    public void updateProfile() throws IOException, InterruptedException {

        wrongPassword.setText(null);
        errorEmailMessage.setText(null);

        FrontendApplication.updateCurrentAccount();
        temp = (User) FrontendApplication.getCurrentAccount();

        temp.setProfileImg(profileImg);
        temp.setPrivacyWl(privacyWl);
        temp.setPrivacyH(privacyHis);
        temp.setPrivacyFr(privacyFl);
        temp.setPrivacyRe(privacyRev);

        if (!usernametf.getText().isEmpty() && !usernametf.getText().equals(temp.getUsername())) {
            request.get("/user/checkUsername/" + usernametf.getText());
            if(request.getMessage().equals("true")){
                errorEmailMessage.setText("This Username is already in use!");
                return;
            }else if(request.getMessage().equals("false")) {
                temp.setUsername(usernametf.getText());
            }
        }
        if (!fnametf.getText().isEmpty()) {
            temp.setForename(fnametf.getText());
        }
        if (!lnametf.getText().isEmpty()) {
            temp.setSurname(lnametf.getText());
        }
        if (!emailtf.getText().isEmpty() && !emailtf.getText().equals(temp.getEmail())) {
            request.get("/user/checkEmail/"+ emailtf.getText());
            if(request.getMessage().equals("true")) {
                errorEmailMessage.setText("This Email is already in use!");
                return;
            }else if(request.getMessage().equals("false")) {
                temp.setEmail(emailtf.getText());
            }
        }


        if (!currentpw.getText().isEmpty() || !newpw.getText().isEmpty() || !repeatpw.getText().isEmpty()) {

            if (currentpw.getText().isEmpty() || newpw.getText().isEmpty() || repeatpw.getText().isEmpty()) {
                if (currentpw.getText().isEmpty()) {
                    mandatory1.setVisible(true);
                } else {
                    mandatory1.setVisible(false);
                }
                if (newpw.getText().isEmpty()) {
                    mandatory2.setVisible(true);
                } else {
                    mandatory2.setVisible(false);
                }
                if (repeatpw.getText().isEmpty()) {
                    mandatory3.setVisible(true);
                } else {
                    mandatory3.setVisible(false);
                }
                wrongPassword.setText("* Mandatory field");
                return;
            } else {
                mandatory1.setVisible(false);
                mandatory2.setVisible(false);
                mandatory3.setVisible(false);
            }


            if (currentpw.getText().hashCode() == temp.getPassword() && newpw.getText().equals(repeatpw.getText())) {
                temp.setPassword(repeatpw.getText().hashCode());
            } else {
                if (currentpw.getText().hashCode() != temp.getPassword()) {
                    wrongPassword.setText("Current password is wrong!");
                }
                if (!newpw.getText().equals(repeatpw.getText())) {
                    wrongPassword.setText("Passwords do not match!");
                }
                return;
            }
        }


        request.put("/users/create/", gson.toJson(temp));
        if(request.getStatus().is2xxSuccessful()) {
            switchToOwnProfile();
        }
        else {
            wrongPassword.setText(request.getMessage());
        }
    }



    public void wlall() {
        privacyWl = 1;
        watchlistpv.setText("Everyone");
    }

    public void wlfriends() {
        privacyWl = 2;
        watchlistpv.setText("Only Friends");
    }

    public void wlnobody() {
        privacyWl = 3;
        watchlistpv.setText("Only Me");
    }

    public void hall() {
        privacyHis = 1;
        historypv.setText("Everyone");
    }

    public void hfriends() {
        privacyHis = 2;
        historypv.setText("Only Friends");
    }

    public void hnobody() {
        privacyHis = 3;
        historypv.setText("Only Me");
    }

    public void frall() {
        privacyFl = 1;
        friendslistpv.setText("Everyone");
    }

    public void frfriends() {
        privacyFl = 2;
        friendslistpv.setText("Only Friends");
    }

    public void frnobody() {
        privacyFl = 3;
        friendslistpv.setText("Only Me");
    }

    public void reall() {
        privacyRev = 1;
        reviewpv.setText("Everyone");
    }

    public void refriends() {
        privacyRev = 2;
        reviewpv.setText("Only Friends");
    }

    public void renobody() {
        privacyRev = 3;
        reviewpv.setText("Only Me");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        temp = (User) FrontendApplication.getCurrentAccount();
        profileImg = temp.getProfileImg();
        usernametf.setText(temp.getUsername());
        fnametf.setText(temp.getForename());
        lnametf.setText(temp.getSurname());
        emailtf.setText(temp.getEmail());


        if (temp.getProfileImg() != null) {
            byte[] bytes = Base64.getDecoder().decode(temp.getProfileImg());
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            Image image = new Image(bais);
            imageview.setImage(image);
        }

        switch (temp.getPrivacyWl()) {
            case 1:
                watchlistpv.setText("Everyone");
                privacyWl = 1;
                break;
            case 2:
                watchlistpv.setText("Only Friends");
                privacyWl = 2;
                break;
            case 3:
                watchlistpv.setText("Only Me");
                privacyWl = 3;
                break;
        }
        switch (temp.getPrivacyH()) {
            case 1:
                historypv.setText("Everyone");
                privacyHis = 1;
                break;
            case 2:
                historypv.setText("Only Friends");
                privacyHis = 2;
                break;
            case 3:
                historypv.setText("Only Me");
                privacyHis = 3;
                break;
        }
        switch (temp.getPrivacyFr()) {
            case 1:
                friendslistpv.setText("Everyone");
                privacyFl = 1;
                break;
            case 2:
                friendslistpv.setText("Only Friends");
                privacyFl = 2;
                break;
            case 3:
                friendslistpv.setText("Only Me");
                privacyFl = 3;
                break;
        }
        switch (temp.getPrivacyRe()) {
            case 1:
                reviewpv.setText("Everyone");
                privacyRev = 1;
                break;
            case 2:
                reviewpv.setText("Only Friends");
                privacyRev = 2;
                break;
            case 3:
                reviewpv.setText("Only Me");
                privacyRev = 3;
                break;
        }
    }
}

package com.frontend.controller;

import com.frontend.FrontendApplication;
import com.frontend.model.Client;
import com.frontend.model.Requests;
import com.frontend.model.Systemadministrator;
import com.frontend.model.User;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.ResourceBundle;


public class LoginController extends SceneController implements Initializable {

    @FXML
    private TextField lotfEnterEmailField;
    @FXML
    private PasswordField lopfEnterPasswordField;
    @FXML
    private Label lolWrongData;
    @FXML
    private Label lolEmptyField;


    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfFirstname;
    @FXML
    private TextField tfLastname;
    @FXML
    private TextField tfEmailRegister;
    @FXML
    private PasswordField pfPasswordRegister;
    @FXML
    private PasswordField pfPasswordRegister2;
    @FXML
    private PasswordField pfAdminkey;

    @FXML
    private DatePicker datePickerBday;

    @FXML
    private Label lErrorMessage;


    @FXML
    private Label lMandatoryField1;
    @FXML
    private Label lMandatoryField2;
    @FXML
    private Label lMandatoryField3;
    @FXML
    private Label lMandatoryField4;
    @FXML
    private Label lMandatoryField5;

    @FXML
    private Label lMandatoryField6;
    @FXML
    private Label lMandatoryField7;
    @FXML
    private Label lAdminkey;


    @FXML
    private Text textAuthentication;
    @FXML
    private TextField tfCode;
    @FXML
    private Text wrongCodeText;


    private Systemadministrator admin = new Systemadministrator();
    private final int adminKey = 123;
    private User user = new User();
    private static String authCode = null;
    private Requests request = new Requests();
    private Gson gson = new Gson();


    public void loginAdmin() throws IOException, InterruptedException {

        lolEmptyField.setText(null);
        lolWrongData.setText(null);
        if (lotfEnterEmailField.getText().isEmpty() || lopfEnterPasswordField.getText().isEmpty()) {
            lolEmptyField.setText("Please fill in all fields. ");
        } else {
            admin.setEmail(lotfEnterEmailField.getText());
            admin.setPassword(lopfEnterPasswordField.getText().hashCode());

            request.post("/systemadministrators/login/", gson.toJson(admin));

            if (request.getStatus().is2xxSuccessful()) {
                authCode = request.getMessage();

                request.get("/systemadministrators/");
                String adminAsJson = request.getMessage();
                Systemadministrator[] adminArray = gson.fromJson(adminAsJson, Systemadministrator[].class);
                for (Systemadministrator admin : adminArray) {
                    if (admin.getEmail().equals(this.admin.getEmail())) {
                        FrontendApplication.setCurrentAccount(admin);
                    }
                }

                switchToAuth();
                System.out.println(authCode);
            }

            if (request.getStatus().is4xxClientError()) {
                lolWrongData.setText(request.getMessage());
            }
        }


    }


    public void loginUser() throws IOException, InterruptedException {

        lolEmptyField.setText(null);
        lolWrongData.setText(null);
        if (lotfEnterEmailField.getText().isEmpty() || lopfEnterPasswordField.getText().isEmpty()) {
            lolEmptyField.setText("Please fill in all fields. ");
        } else {

            user.setEmail(lotfEnterEmailField.getText());
            user.setPassword(lopfEnterPasswordField.getText().hashCode());

            request.post("/users/login/", gson.toJson(user));

            if (request.getStatus().is2xxSuccessful()) {
                authCode = request.getMessage();
                request.get("/users/");
                String userAsJson = request.getMessage();
                User[] userArray = gson.fromJson(userAsJson, User[].class);
                for (User user : userArray) {
                    if (user.getEmail().equals(this.user.getEmail())) {
                        FrontendApplication.setCurrentAccount(user);

                    }
                }
                switchToAuth();

                System.out.println(authCode);
            }

            if (request.getStatus().is4xxClientError()) {
                lolWrongData.setText(request.getMessage());
            }
        }

    }


    public void authentication() throws IOException, InterruptedException {

        if (tfCode.getText().equals(authCode)) {

            if (!FrontendApplication.getCurrentAccount().isAdmin()) {
                user = (User) FrontendApplication.getCurrentAccount();

                FrontendApplication.setCurrentClient(new Client(new Socket("localhost", 1234)));
                FrontendApplication.getCurrentClient().listenMessage();
                request.put("/users/update/"+user.getAccountID()+"/true", "");
                switchToStart();
            } else {
                switchToMovielist();
            }


        } else {
            wrongCodeText.setText("Check your input!");
        }
    }

    public void signUpAdmin() {

        lMandatoryField1.setText(null);
        lMandatoryField2.setText(null);
        lMandatoryField3.setText(null);
        lMandatoryField4.setText(null);
        lMandatoryField5.setText(null);
        lErrorMessage.setText(null);
        lAdminkey.setText(null);

        if (tfFirstname.getText().isEmpty() || tfLastname.getText().isEmpty() || tfEmailRegister.getText().isEmpty() ||
                pfPasswordRegister.getText().isEmpty() || pfPasswordRegister2.getText().isEmpty()) {

            lErrorMessage.setText("* Mandatory Field");

            if (tfFirstname.getText().isEmpty()) {
                lMandatoryField1.setText("*");
            }

            if (tfLastname.getText().isEmpty()) {
                lMandatoryField2.setText("*");
            }

            if (tfEmailRegister.getText().isEmpty()) {
                lMandatoryField3.setText("*");
            }
            if (pfPasswordRegister.getText().isEmpty()) {
                lMandatoryField4.setText("*");
            }
            if (pfPasswordRegister2.getText().isEmpty()) {
                lMandatoryField5.setText("*");

            }
            if (pfAdminkey.getText().isEmpty()) {
                lAdminkey.setText("*");
            }

        } else if (!pfPasswordRegister.getText().equals(pfPasswordRegister2.getText())) {
            lErrorMessage.setText("Password does not match.");
        } else if (!pfAdminkey.getText().equals(Integer.toString(adminKey))) {
            lErrorMessage.setText("Adminkey is invalid. ");
        } else if (pfAdminkey.getText().equals(Integer.toString(adminKey))) {
            Systemadministrator newAdmin = new Systemadministrator();
            newAdmin.setForename(tfFirstname.getText());
            newAdmin.setSurname(tfLastname.getText());
            newAdmin.setEmail(tfEmailRegister.getText());
            newAdmin.setPassword(pfPasswordRegister.getText().hashCode());


            try {
                request.post("/systemadministrators/create/", gson.toJson(newAdmin));

                if (request.getStatus().is2xxSuccessful()) {

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText(request.getMessage());
                    alert.setTitle("Registration successful");
                    alert.setResizable(false);
                    switchToLogin();
                    alert.show();


                }
            } catch (Exception exc) {
                exc.printStackTrace();
            }
            if (request.getStatus().is4xxClientError()) {
                lMandatoryField3.setText(request.getMessage());
            }
        }
    }


    public void signUpUser() {

        lMandatoryField1.setText(null);
        lMandatoryField2.setText(null);
        lMandatoryField3.setText(null);
        lMandatoryField4.setText(null);
        lMandatoryField5.setText(null);
        lMandatoryField6.setText(null);
        lMandatoryField7.setText(null);
        lErrorMessage.setText(null);


        if (tfUsername.getText().isEmpty() || tfFirstname.getText().isEmpty() || tfLastname.getText().isEmpty() ||
                tfEmailRegister.getText().isEmpty() || datePickerBday.getValue() == null || pfPasswordRegister.getText().isEmpty() ||
                pfPasswordRegister2.getText().isEmpty() || tfUsername.getText().isEmpty()) {

            lErrorMessage.setText("* Mandatory Field");

            if (tfUsername.getText().isEmpty()) {
                lMandatoryField1.setText("*");
            }

            if (tfFirstname.getText().isEmpty()) {
                lMandatoryField2.setText("*");
            }

            if (tfLastname.getText().isEmpty()) {
                lMandatoryField3.setText("*");
            }
            if (datePickerBday.getValue() == null) {
                lMandatoryField4.setText("*");
            }
            if (tfEmailRegister.getText().isEmpty()) {
                lMandatoryField5.setText("*");
            }
            if (pfPasswordRegister.getText().isEmpty()) {
                lMandatoryField6.setText("*");
            }
            if (pfPasswordRegister2.getText().isEmpty()) {
                lMandatoryField7.setText("*");
            }


        } else if (!pfPasswordRegister.getText().equals(pfPasswordRegister2.getText())) {
            lErrorMessage.setText("Password does not match.");

        } else {
            User newUser = new User();
            newUser.setUsername(tfUsername.getText());
            newUser.setForename(tfFirstname.getText());
            newUser.setSurname(tfLastname.getText());
            newUser.setEmail(tfEmailRegister.getText());
            String myFormattedDate = datePickerBday.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            newUser.setDateOfBirth(myFormattedDate);
            newUser.setPassword(pfPasswordRegister.getText().hashCode());


            try {

                byte[] byteArray = IOUtils.toByteArray(getClass().getResourceAsStream("/profile.png"));
                String profileImg = Base64.getEncoder().encodeToString(byteArray);
                newUser.setProfileImg(profileImg);

                request.post("/users/create/", gson.toJson(newUser));

                if (request.getStatus().is2xxSuccessful()) {

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText(request.getMessage());
                    alert.setTitle("Registration successful");
                    alert.setResizable(false);
                    switchToLogin();
                    alert.show();


                }
            } catch (Exception exc) {
                exc.printStackTrace();
            }
            if (request.getStatus().is4xxClientError()) {
                lErrorMessage.setText(request.getMessage());
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (FrontendApplication.getCurrentAccount() != null) {
            try {
                textAuthentication.setText("We have sent you a security code to " + FrontendApplication.getCurrentAccount().getEmail());
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
    }
}








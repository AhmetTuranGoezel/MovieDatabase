package com.frontend.model;

import com.frontend.controller.SceneController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class Systemadministrator extends Account {

    public Systemadministrator() {
        setAdmin(true);
    }

    public Systemadministrator(String forename, String surname, String email, long password) {
        super(forename, surname, email, password);
        setAdmin(true);
    }
}

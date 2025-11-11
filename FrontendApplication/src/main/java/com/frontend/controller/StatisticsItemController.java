package com.frontend.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StatisticsItemController {


    @FXML
    private Label labelRank;
    @FXML
    private Label labelName;



    public void setRank(int rank){
        labelRank.setText("#" + rank);
    }

    public void setName(String fav){
        this.labelName.setText(fav);
    }
}

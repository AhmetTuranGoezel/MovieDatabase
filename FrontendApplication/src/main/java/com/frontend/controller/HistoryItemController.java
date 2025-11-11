package com.frontend.controller;

import com.frontend.FrontendApplication;

public class HistoryItemController extends MovieItemController{


    public void openDetails(){
        StartController sc = FrontendApplication.getCurrentFxmlLoader().getController();

        sc.resetButtons();

        sc.setVisibilityInvitationButton(true);
        sc.setVisibilityReportButton(true);
        sc.setVisibilityReviewButton(true);

        sc.setVisibilityWatchlistButton(false);
        sc.setVisibilityHistoryButton(false);


        sc.showDetails(movie);

    }
}

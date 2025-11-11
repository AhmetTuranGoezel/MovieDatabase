package com.frontend.controller;

import com.frontend.FrontendApplication;

public class WatchlistItemController extends MovieItemController{


    public void openDetails(){
        StartController sc = FrontendApplication.getCurrentFxmlLoader().getController();

        sc.resetButtons();

        sc.setVisibilityHistoryButton(true);
        sc.setVisibilityInvitationButton(true);
        sc.setVisibilityReportButton(true);

        sc.setVisibilityWatchlistButton(false);
        sc.setVisibilityReviewButton(false);

        sc.showDetails(movie);

    }
}

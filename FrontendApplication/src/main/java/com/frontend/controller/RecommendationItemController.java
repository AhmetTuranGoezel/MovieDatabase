package com.frontend.controller;

import com.frontend.FrontendApplication;

public class RecommendationItemController extends MovieItemController{

    public void openDetails(){
        StartController sc = FrontendApplication.getCurrentFxmlLoader().getController();

        sc.resetButtons();

        sc.setVisibilityInvitationButton(true);
        sc.setVisibilityReportButton(true);
        sc.setVisibilityWatchlistButton(true);
        sc.setVisibilityHistoryButton(true);

        sc.setVisibilityReviewButton(false);

        sc.showDetails(movie);

    }
}

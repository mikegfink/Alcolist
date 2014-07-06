package com.lamchop.alcolist.client.ui;

import com.lamchop.alcolist.client.AppDataController;

public class UserDataPanels {
	private ReviewPanel reviewPanel;
	private RatingPanel ratingPanel;
	private RoutePanel routePanel;
	
	public UserDataPanels(AppDataController appDataController) {
		//setReviewPanel(new ReviewPanel(appDataController));
		setRatingPanel(new RatingPanel(appDataController));
		setRoutePanel(new RoutePanel(appDataController));		
	}

	public RoutePanel getRoutePanel() {
		return routePanel;
	}

	public void setRoutePanel(RoutePanel routePanel) {
		this.routePanel = routePanel;
	}

	public ReviewPanel getReviewPanel() {
		return reviewPanel;
	}

	public void setReviewPanel(ReviewPanel reviewPanel) {
		this.reviewPanel = reviewPanel;
	}

	public RatingPanel getRatingPanel() {
		return ratingPanel;
	}

	public void setRatingPanel(RatingPanel ratingPanel) {
		this.ratingPanel = ratingPanel;
	}
	
	
}

package com.lamchop.alcolist.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.lamchop.alcolist.client.ui.UIUpdateInterface;
import com.lamchop.alcolist.shared.Manufacturer;
import com.lamchop.alcolist.shared.Rating;
import com.lamchop.alcolist.shared.Review;
import com.lamchop.alcolist.shared.Route;

public class AppDataController {

	private AppData appData;
	private UIUpdateInterface theUI;
	private static final UserDataServiceAsync 
							userDataService	 = GWT.create(UserDataService.class);
	private static final ManufacturerServiceAsync 
							manufacturerService = GWT.create(ManufacturerService.class);

	private List<Manufacturer> displayedManufacturers;
	
	public AppDataController(UIUpdateInterface theUI) {
		appData = new AppData();
		this.theUI = theUI;
	}

	// TODO: Request rework of UserData storage so that we receive a UserData object from 
	// the server
	public void initUserData(String userID, String userName) {
		appData.newUserData(userID, userName);
		getRatings(userID);
	}

	private void getRatings(final String userID) {
		userDataService.getRatings(userID, (new AsyncCallback<List<Rating>>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(List<Rating> result) {
				updateUserDataRatings(result);
				getReviews(userID);
			}
		}));
	}
	
	private void getReviews(final String userID) {
		userDataService.getReviews(userID, (new AsyncCallback<List<Review>>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(List<Review> result) {
				updateUserDataReviews(result);
				getRoutes(userID);
			}
		}));
	}
	
	private void getRoutes(final String userID) {
		userDataService.getRoutes(userID, (new AsyncCallback<List<Route>>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(List<Route> result) {
				updateUserDataRoutes(result);
				sendUserDataToUI();
			}
		}));
	}
	
	public void updateUserDataReviews(List<Review> reviews) {
		for (Review review : reviews) {
			appData.addReview(review);
		}
	}
	
	public void updateUserDataRatings(List<Rating> ratings) {
		for (Rating rating : ratings) {
			appData.addRating(rating);
		}
	}
	
	public void updateUserDataRoutes(List<Route> routes) {
		for (Route route : routes) {
			appData.addRoute(route);
		}
	}
	// TODO: All methods above hopefully collapsed into one if UserDataServices are reworked
	
	public void initManufacturers() {		
		manufacturerService.getManufacturers(new AsyncCallback<List<Manufacturer>>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(List<Manufacturer> result) {
				updateAppDataManufacturers(result);
				sendManufacturersToUI();
				displayedManufacturers = result;
			}
		});		
	}

	public void deleteManufacturers() {
		clearManufacturers();
		sendManufacturersToUI();
	}

	private void clearManufacturers() {
		appData.clearManufacturers();
	}

	private void updateAppDataManufacturers(List<Manufacturer> manufacturers) {		
		clearManufacturers();
		appData.add(manufacturers);
	}

	private void updateAppDataUserData(UserData userData) {		
		appData.setUserData(userData);
	}

	public void sendManufacturersToUI() {		
		theUI.update(appData.getManufacturers());
	}

	public void sendUserDataToUI() {
		theUI.update(appData.getUserData());	
	}

	public void clearUserData() {		
		appData.clearUserData();	
		sendUserDataToUI();
	}	

	public boolean isUserLoggedIn() {
		return appData.isLoggedIn();
	}

	public void filterBySearch(String searchText) {
//		List<Manufacturer> allManufacturers = appData.getManufacturers();
		List<Manufacturer> filteredManufacturers = new ArrayList<Manufacturer>();
		for (Manufacturer m : displayedManufacturers) {
			if (m.getCity().toLowerCase().contains(searchText) || m.getName().toLowerCase().contains(searchText) || m.getFullAddress().toLowerCase().contains(searchText))
				filteredManufacturers.add(m);	
		}
		theUI.update(filteredManufacturers);

	}

	public Review addReview(String reviewText, final String manID) {
		// TODO Auto-generated method stub
		Review review = appData.addReview(manID, reviewText);
		
		userDataService.addReview(review, (new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(Void result) {
				GWT.log("Review added successfully for: Manufacturer ID: " + manID);
			}
		}));
		
		return review;
	}
	
	public void addRating(int ratingValue, final String manID) {
		// TODO Auto-generated method stub
		Rating rating = appData.addRating(manID, ratingValue);
		
		userDataService.addRating(rating, (new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(Void result) {
				GWT.log("Rating added successfully for: Manufacturer ID: " + manID);
			}
		}));		
	}
	
	private void handleError(Throwable error) {
		GWT.log(error.getMessage());
		/*if (error instanceof NotLoggedInException) {
			Window.Location.replace(loginInfo.getLogoutUrl());*/
	}
	
	public void filterByType(String type) {
		List<Manufacturer> allManufacturers = appData.getManufacturers();
		List<Manufacturer> filteredManufacturers = new ArrayList<Manufacturer>();
		for (Manufacturer m : allManufacturers) {
			if (m.getType().equals(type))
				filteredManufacturers.add(m);	
		}
		theUI.update(filteredManufacturers);
		displayedManufacturers = filteredManufacturers;
	}
	
	public void removeFilter() {
		displayedManufacturers = appData.getManufacturers();
		theUI.update(displayedManufacturers);
	}

	public Review getReview(String manID) {
		return appData.getReview(manID);
	}
}

package com.lamchop.alcolist.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.lamchop.alcolist.client.ui.MyLocation;
import com.lamchop.alcolist.client.ui.UIUpdateInterface;
import com.lamchop.alcolist.shared.Manufacturer;
import com.lamchop.alcolist.shared.Rating;
import com.lamchop.alcolist.shared.Review;
import com.lamchop.alcolist.shared.Route;

public class AppDataController {

	private static final int METER_TO_LATLNG = 10000;
	private AppData appData;
	private UIUpdateInterface theUI;
	private static final UserDataServiceAsync 
	userDataService	 = GWT.create(UserDataService.class);
	private static final ManufacturerServiceAsync 
	manufacturerService = GWT.create(ManufacturerService.class);

	private MyLocation myLocation; 
	private List<Manufacturer> allManufacturers;
	private List<Manufacturer> displayList;
	private String type;
	private String searchText;
	private boolean nearMe;
	private boolean firstNearMe;

	public AppDataController(UIUpdateInterface theUI) {
		appData = new AppData();
		this.theUI = theUI;
		displayList = new ArrayList<Manufacturer>();
		myLocation = null;
		nearMe = false;
		firstNearMe = true;
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
				allManufacturers = result;
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
		this.searchText = searchText;
		filter();
	}

	private void searchList() {		
		Iterator<Manufacturer> iter = displayList.iterator();
		while (iter.hasNext()) {
			Manufacturer m = iter.next();
			if (!hasSearchText(m))
				iter.remove();	
			}
	}

	private boolean hasSearchText(Manufacturer m) {
		return m.getCity().toLowerCase().contains(searchText) || m.getName().toLowerCase().contains(searchText) || m.getFullAddress().toLowerCase().contains(searchText);
	}

	public void filterByType(String type) {		
		this.type = type;
		filter();
	}

	private void filterList() {		
		Iterator<Manufacturer> iter = displayList.iterator();
		while (iter.hasNext()) {
			Manufacturer m = iter.next();
			if (!m.getType().equals(type))
				iter.remove();	
			}
	}

	public void removeFilter() {
		type = null;
		filter();
	}

	public void removeSearch() {
		searchText = null;
		filter();
	}

	public void showNearMe() {
		if (firstNearMe) {
			myLocation = new MyLocation(this);
		}
		else {
			nearMe = true;
			filter();
		}
	}

	public void firstNearMe() {
		firstNearMe = false;
		nearMe = true;
		filter();
	}

	private void nearMeList() {
		Iterator<Manufacturer> iter = displayList.iterator();
		while (iter.hasNext()) {
			Manufacturer m = iter.next();
			double dist = distFrom(m.getLatitude(), m.getLongitude(), 
					myLocation.getMyLocation().getLatitude(), 
					myLocation.getMyLocation().getLongitude());
			if (!isNearMe(dist)) {
				iter.remove();
			}
		}
		
		theUI.showNearMeCircle(myLocation);
	}

	public void clearNearMe() {
		theUI.hideNearMeCircle();
		nearMe = false;
		filter();			
	}

	private boolean isNearMe(double dist) {
		return dist < (MyLocation.NEAR_ME_RADIUS_METERS);			
	}

	private double distFrom(double lat1, double lng1, double lat2, double lng2) {
		double earthRadius = 6371000;
		double dLat = Math.toRadians(lat2-lat1);
		double dLng = Math.toRadians(lng2-lng1);
		double sindLat = Math.sin(dLat / 2);
		double sindLng = Math.sin(dLng / 2);
		double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
				* Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		double dist = earthRadius * c;

		return dist;
	}
	
	private void filter() {
		displayList.clear();
		displayList.addAll(allManufacturers);
		if (type != null) {
			filterList();
		}
		if (searchText != null) {
			searchList();
		}
		if (nearMe) {
			nearMeList();
		}

		theUI.update(displayList);
	}

	public Review addReview(String reviewText, final String manID) {
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
		Rating rating = appData.addRating(manID, ratingValue);
		GWT.log("Rating was: " + rating.getRating() + " for ID: " + manID);
		userDataService.addRating(rating, (new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(Void result) {
				GWT.log("Rating: added successfully for: Manufacturer ID: " + manID);
			}
		}));		
	}

	public Review getReview(String manID) {
		return appData.getReview(manID);
	}

	public Rating getRating(String manID) {
		return appData.getRating(manID);
	}

	private void handleError(Throwable error) {
		GWT.log(error.getMessage());
	}
}

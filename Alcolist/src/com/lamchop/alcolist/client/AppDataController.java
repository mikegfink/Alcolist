package com.lamchop.alcolist.client;

import java.util.ArrayList;
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
	private List<Manufacturer> filteredList;
	private List<Manufacturer> searchList;
	private List<Manufacturer> allManufacturers;
	private List<Manufacturer> nearMeList;
	private String type;
	private String searchText;
	
	public AppDataController(UIUpdateInterface theUI) {
		appData = new AppData();
		this.theUI = theUI;
		searchList = new ArrayList<Manufacturer>();
		filteredList = new ArrayList<Manufacturer>();
		nearMeList = new ArrayList<Manufacturer>();
		myLocation = null;
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
		if (type != null && searchText != null)
			filterList(allManufacturers);
		this.searchText = searchText;
		if (filteredList.isEmpty() && nearMeList.isEmpty())
			searchList(allManufacturers);
		else if (nearMeList.isEmpty()) {
			searchList(filteredList);
		} else {
			filterNearMe(myLocation);
			searchList(nearMeList);
		}
		
		theUI.update(searchList);
	}
	
	private void searchList(List<Manufacturer> manufacturers) {
		searchList.clear();
		
		for (Manufacturer m : manufacturers) {
			if (m.getCity().toLowerCase().contains(searchText) || m.getName().toLowerCase().contains(searchText) || m.getFullAddress().toLowerCase().contains(searchText))
				searchList.add(m);	
		}
	}
	
	public void filterByType(String type) {
		if (searchText != null)
			searchList(allManufacturers);
		
		this.type = type;
		
		if (searchList.isEmpty() && nearMeList.isEmpty()) {
			filterList(allManufacturers);
		} else if (nearMeList.isEmpty()) {
			filterList(searchList);
		} else {
			filteredList.clear();
			filterNearMe(myLocation);
			filterList(nearMeList);
		}
		
		theUI.update(filteredList);
	}
	
	private void filterList(List<Manufacturer> manufacturers) {
		filteredList.clear();
		
		for (Manufacturer m : manufacturers) {
			if (m.getType().equals(type))
				filteredList.add(m);	
		}		
	}
	
	public void removeFilter() {
		filteredList.clear();
		type = null;
		
		if (searchList.isEmpty() && nearMeList.isEmpty()) {
			theUI.update(allManufacturers);
		}
		else if (nearMeList.isEmpty()){
			searchList(allManufacturers);
			theUI.update(searchList);
		} else if (searchList.isEmpty()) {
			filterNearMe(myLocation);
			updateNearMe(myLocation);
		} else {
			searchList(allManufacturers);
			filterNearMe(myLocation);
			updateNearMe(myLocation);
		}
	}
	
	public void removeSearch() {
		searchList.clear();
		searchText = null;
		
		if (filteredList.isEmpty() && nearMeList.isEmpty())
			theUI.update(allManufacturers);
		else if (nearMeList.isEmpty()) {
			filterList(allManufacturers);
			theUI.update(filteredList);
		} else if (filteredList.isEmpty()) {
			filterNearMe(myLocation);
			updateNearMe(myLocation);
		} else {
			filterList(allManufacturers);
			filterNearMe(myLocation);
			updateNearMe(myLocation);
		}
	}
	
	public void showNearMe() {
		myLocation = new MyLocation(this);
		
		GWT.log("LatLng is: " + myLocation.toString());
		
	}

	public void filterNearMe(MyLocation myLocation) {
		LatLng myLatLng = myLocation.getMyLocation();
		List<Manufacturer> baseList;
		
		if (filteredList.isEmpty()) {
			if (searchList.isEmpty()) {
				baseList = allManufacturers;
			} else {
				baseList = searchList;
			}
		}
		else {
			baseList = filteredList;
		}
		
		for (Manufacturer m : baseList) {
			double dist = distFrom(m.getLatitude(), m.getLongitude(), myLatLng.getLatitude(), myLatLng.getLongitude());
			if (isNearMe(dist)) {
				nearMeList.add(m);
			}
		}	
		
	}

	public void updateNearMe(MyLocation myLocation) {
		theUI.update(nearMeList);
		theUI.showNearMeCircle(myLocation);
	}
	
	public void clearNearMe() {
		nearMeList.clear();
		theUI.hideNearMeCircle();
		filterByType(type);			
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

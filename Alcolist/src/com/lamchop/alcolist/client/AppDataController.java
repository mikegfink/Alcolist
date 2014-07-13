package com.lamchop.alcolist.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
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

	private MyLocation myLocation; 
	private List<Manufacturer> allManufacturers;
	private List<Manufacturer> displayList;
	private String type;
	private String searchText;
	private boolean nearMe;
	private boolean firstNearMe;
	private MultiWordSuggestOracle routeOracle; 
	private boolean visited;

	public AppDataController(UIUpdateInterface theUI) {
		appData = new AppData();
		this.theUI = theUI;
		displayList = new ArrayList<Manufacturer>();
		allManufacturers = new ArrayList<Manufacturer>();
		myLocation = null;
		nearMe = false;
		firstNearMe = true;
		visited = false;
	}

	public void createOracle() {
		routeOracle = new MultiWordSuggestOracle();
		for (Manufacturer m: allManufacturers) {
			routeOracle.add(m.getName());
		}
	}
	
	public MultiWordSuggestOracle getOracle() {
		return routeOracle;
	}
	
	public Manufacturer findTheManufacturer(String name) {
		
		for (Manufacturer m: allManufacturers) {
			if (m.getName().toLowerCase().equals(name.toLowerCase())) {
				return m;
			}
		}
		return null;
	}
	
	public void initUserData(String userID, String userName) {
		appData.newUserData(userID, userName);
		retrieveRatings(userID);
	}

	private void retrieveRatings(final String userID) {
		userDataService.getRatings(userID, (new AsyncCallback<List<Rating>>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(List<Rating> result) {
				updateUserDataRatings(result);
				retrieveReviews(userID);
			}
		}));
	}

	private void retrieveReviews(final String userID) {
		userDataService.getReviews(userID, (new AsyncCallback<List<Review>>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(List<Review> result) {
				updateUserDataReviews(result);
				retrieveRoutes(userID);
			}
		}));
	}

	private void retrieveRoutes(final String userID) {
		userDataService.getRoutes(userID, (new AsyncCallback<List<Route>>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(List<Route> result) {
				updateUserDataRoutes(result);
				retrieveAdmin(userID);
			}
		}));
	}
	
	private void retrieveAdmin(final String userID) {
		userDataService.isAdmin(userID, (new AsyncCallback<Boolean>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(Boolean result) {
				appData.setAdmin(result);
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

	public void initManufacturers() {		
		manufacturerService.getManufacturers(new AsyncCallback<List<Manufacturer>>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(List<Manufacturer> result) {
				updateAppDataManufacturers(result);
				sendManufacturersToUI();
				allManufacturers = result;
				createOracle();
			}
		});		
	}

	public void deleteManufacturers() {
		clearManufacturers();
		sendManufacturersToUI();
	}

	private void clearManufacturers() {
		appData.clearManufacturers();
		allManufacturers.clear();
		displayList.clear();
	}

	private void updateAppDataManufacturers(List<Manufacturer> manufacturers) {		
		clearManufacturers();
		appData.add(manufacturers);
	}

	public void sendManufacturersToUI() {		
		theUI.update(appData.getManufacturers());
	}

	public void sendUserDataToUI() {
		theUI.update(appData.getUserData());	
	}

	public void clearUserData() {		
		appData.clearUserData();
		clearVisited();
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
		if (displayList.size() != allManufacturers.size())
			filter();
	}
	
	public void showVisited() {
		visited = true;
		filter();
	}
	
	public void clearVisited() {
		visited = false;
		filter();
	}
	
	private void visitedList() {
		Iterator<Manufacturer> iter = displayList.iterator();
		while (iter.hasNext()) {
			Manufacturer m = iter.next();
			if (!isVisited(m)) {
				iter.remove();
			}
		}
	}
	
	private boolean isVisited(Manufacturer m) {
		return (getRating(m.getID()) != null || getReview(m.getID()) != null);
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
			double dist = myLocation.distFrom(m.getLatitude(), m.getLongitude());
			if (!myLocation.isNearMe(dist)) {
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
		if (visited) {
			visitedList();
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

	public void addRating(int ratingValue, final Manufacturer manufacturer) {
		Rating rating = appData.addRating(manufacturer, ratingValue);
		GWT.log("Rating was: " + rating.getRating() + " for ID: " + manufacturer);
			
		userDataService.addRating(rating, (new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(Void result) {
				GWT.log("Rating added successfully for: Manufacturer ID: " + manufacturer);
			}
		}));	
		
		manufacturerService.addManufacturer(manufacturer, (new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				handleError(error);
				GWT.log("Manufacturer: " + manufacturer.getName() + " failed to update.");
			}

			public void onSuccess(Void result) {
				GWT.log("Manufacturer: " + manufacturer.getName() + " stored successfully.");
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
	
	public List<Route> getRoutes() {
		return appData.getRoutes();
	}

	public void addRoute(Route theRoute, String routeName) {
		theRoute.setRouteName(routeName);
		theRoute.setUserID(appData.getUserData().getUserID());
		
		appData.addRoute(theRoute);
		userDataService.addRoute(theRoute, (new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(Void result) {
				GWT.log("Route added successfully");
			}
		}));	
		
	}
	
	public void removeRoute(Route route) {
		appData.removeRoute(route);
		userDataService.removeRoute(route, (new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(Void result) {
				GWT.log("Route deleted successfully");
			}
		}));
	}
}

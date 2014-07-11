package com.lamchop.alcolist.client;

import java.util.ArrayList;
import java.util.List;

import com.lamchop.alcolist.shared.Manufacturer;
import com.lamchop.alcolist.shared.Rating;
import com.lamchop.alcolist.shared.Review;
import com.lamchop.alcolist.shared.Route;

final class AppData {
	private List<Manufacturer> manufacturers;
	private UserData userData;
	
	public AppData() {
		manufacturers = new ArrayList<Manufacturer>();
		userData = new UserData();
	}
	
	public void add(List<Manufacturer> manufacturersToAdd) {
		for (Manufacturer nManufacturer : manufacturersToAdd) {
			if (!manufacturers.contains(nManufacturer)) {
				manufacturers.add(nManufacturer);
			}
		}
	}
	
	public List<Manufacturer> getManufacturers() {
		return manufacturers;
	}

	public Boolean isLoggedIn() {
		return !userData.isDefault();
	}
	
	public void setAdmin(boolean isAdmin) {
		userData.setAdmin(isAdmin);
	}
	
	public boolean isAdmin() {
		return userData.isAdmin();
	}

	public UserData getUserData() {
		return userData;
	}
	
	public void newUserData(String userID, String userName) {
		clearUserData();
		userData.setName(userName);
		userData.setUserID(userID);
	}
	
	public void clearUserData() {
		userData.setIDToDefault();
		userData.setNameToDefault();
		userData.clearRatings();
		userData.clearRoutes();
		userData.clearReviews();
	}
	
	public void setUserData(UserData userData) {
		this.userData = userData;
	}
	
	public void addRating(Rating rating) {
		userData.add(rating);
	}
	
	public void addReview(Review review) {
		userData.add(review);
	}
	
	public void addRoute(Route route) {
		userData.add(route);
	}
	
	public Review addReview(String manID, String reviewText) {
		Review review = new Review(userData.getUserID(), manID, reviewText);
		userData.add(review);
		return review;
	}
	
	public Rating addRating(Manufacturer manufacturer, int ratingValue) {
		Rating rating = new Rating(userData.getUserID(), manufacturer.getID(), ratingValue);
		Rating oldRating = userData.add(rating);
		if (oldRating != null) {
			manufacturer.removeRating(oldRating.getRating());			
		}
		manufacturer.addRating(ratingValue);		
		return rating;
	}

	public void clearManufacturers() {
		manufacturers.clear();		
	}
	
	public Review getReview(String manID) {
		return userData.findReview(manID);
	}

	public Rating getRating(String manID) {
		return userData.findRating(manID);
	}

	public List<Route> getRoutes() {
		return userData.getRoutes();
	}
}

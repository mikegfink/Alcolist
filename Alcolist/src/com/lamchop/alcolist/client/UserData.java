package com.lamchop.alcolist.client;

import java.util.ArrayList;
import java.util.List;

import com.lamchop.alcolist.shared.Rating;
import com.lamchop.alcolist.shared.Review;
import com.lamchop.alcolist.shared.Route;

public class UserData {
	
	private static final String DEFAULT_NAME = "Guest";
	private static final String DEFAULT_USERID = "";
	
	private String userID; 
	private String name;
	private List<Rating> ratings;
	private List<Review> reviews;
	private List<Route> routes;
	private boolean isAdmin;

	public UserData() {
		this.userID = DEFAULT_USERID;
		name = DEFAULT_NAME;
		ratings = new ArrayList<Rating>();
		reviews = new ArrayList<Review>();
		routes = new ArrayList<Route>();
		isAdmin = false;
	}
	
	public Rating add(Rating rating) {
		// If manufacturer has a rating, replace it, otherwise add it.
		for (Rating aRating : ratings) {
			if (aRating.getManufacturerID().equals(rating.getManufacturerID())) {
				ratings.remove(aRating);
				ratings.add(rating);
				return aRating;
			}
		}
		ratings.add(rating);	
		return null;
	}
	
	public void remove(Rating rating) {
		// Remove a rating from storage.
		ratings.remove(rating);
	}

	public void add(Review review) {
		// If manufacturer has a review, replace it, otherwise add it.
		for (Review aReview : reviews) {
			if (aReview.getManufacturerID().equals(review.getManufacturerID())) {
				reviews.remove(aReview);
				reviews.add(review);
				return;
			}
		}
		reviews.add(review);
	}
	
	public void remove(Review review) {
		// Remove a review from storage.
		reviews.remove(review);
	}

	public void add(Route route) {
		// If route ID already exists replace it, otherwise new route.
		// How is this going to work?
		for (Route aRoute : routes) {
			if (aRoute.getID().equals(route.getID())) {
				routes.remove(aRoute);
				routes.add(route);
				return;
			}
		}
		routes.add(route);
	}
	
	public void remove(Route route) {
		// Remove a route from storage.
		routes.remove(route);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserID() {
		return userID;
	}
	
	public void setNameToDefault() {
		name = DEFAULT_NAME;		
	}
	
	public void setIDToDefault() {
		userID = DEFAULT_USERID;
	}
	
	public void clearRatings() {
		ratings.clear();
	}
	
	public void clearRoutes() {
		routes.clear();		
	}
	
	public void clearReviews() {
		reviews.clear();		
	}

	public Boolean isDefault() {
		return (userID.equals(DEFAULT_USERID) && name.equals(DEFAULT_NAME));
	}

	public Review findReview(String manID) {
		for (Review aReview : reviews) {
			if (aReview.getManufacturerID().equals(manID)) {
				return aReview;
			}
		}	
		return null;
	}

	public Rating findRating(String manID) {
		for (Rating aRating : ratings) {
			if (aRating.getManufacturerID().equals(manID)) {
				return aRating;
			}
		}	
		return null;
	}
	
	public List<Route> getRoutes() {
		return routes;
	}
	public List<Rating> getRatings() {
		return ratings;
	}
	public List<Review> getReviews() {
		return reviews;
	}
	
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean isAdmin() {
		// TODO Auto-generated method stub
		return isAdmin;
	}
	
}

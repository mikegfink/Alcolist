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
	
	public UserData() {
		this.userID = DEFAULT_USERID;
		name = DEFAULT_NAME;
		ratings = new ArrayList<Rating>();
		reviews = new ArrayList<Review>();
		routes = new ArrayList<Route>();
	}
	
	public void add(Rating rating) {
		// TODO Auto-generated method stub
		// If manufacturer has a rating, replace it, otherwise add it.
	}
	
	public void remove(Rating rating) {
		// TODO Auto-generated method stub
		// Remove a rating from storage.
	}

	public void add(Review review) {
		// TODO Auto-generated method stub
		// If manufacturer has a review, replace it, otherwise add it.
	}
	
	public void remove(Review review) {
		// TODO Auto-generated method stub
		// Remove a review from storage.
	}

	public void add(Route route) {
		// TODO Auto-generated method stub
		// If route ID already exists replace it, otherwise new route.
		// How is this going to work?
	}
	
	public void remove(Route route) {
		// TODO Auto-generated method stub
		// Remove a route from storage.
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
}

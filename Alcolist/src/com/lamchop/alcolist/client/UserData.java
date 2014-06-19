package com.lamchop.alcolist.client;

import java.util.List;

import com.lamchop.alcolist.shared.Rating;
import com.lamchop.alcolist.shared.Review;
import com.lamchop.alcolist.shared.Route;

public class UserData {
	
	private String userID; // Maybe unnecessary?
	private String name;
	private List<Rating> ratings;
	private List<Review> reviews;
	private List<Route> routes;
	
	public UserData(String userID, String userName) {
		this.userID = userID;
		name = userName;		
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

}

package com.lamchop.alcolist.client;

import java.util.List;

import com.lamchop.alcolist.shared.Rating;
import com.lamchop.alcolist.shared.Review;
import com.lamchop.alcolist.shared.Route;

public class UserData {
	
	private int userID; // Maybe unnecessary?
	private List<Rating> ratings;
	private List<Review> reviews;
	private List<Route> routes;
	
	public void add(Rating rating) {
		// TODO Auto-generated method stub
		// If manufacturer has a rating, replace it, otherwise add it.
	}

	public void add(Review review) {
		// TODO Auto-generated method stub
		// If manufacturer has a review, replace it, otherwise add it.
	}

	public void add(Route route) {
		// TODO Auto-generated method stub
		// If route ID already exists replace it, otherwise new route.
		// How is this going to work?
	}

}